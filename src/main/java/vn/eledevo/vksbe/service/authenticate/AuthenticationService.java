package vn.eledevo.vksbe.service.authenticate;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import vn.eledevo.vksbe.config.security.JwtService;
import vn.eledevo.vksbe.constant.Role;
import vn.eledevo.vksbe.constant.TokenType;
import vn.eledevo.vksbe.constant.UserStatus;
import vn.eledevo.vksbe.dto.request.login_register.AuthenticationRequest;
import vn.eledevo.vksbe.dto.request.login_register.RegisterRequest;
import vn.eledevo.vksbe.dto.response.AuthenticationResponse;
import vn.eledevo.vksbe.entity.Token;
import vn.eledevo.vksbe.entity.User;
import vn.eledevo.vksbe.exception.ValidationException;
import vn.eledevo.vksbe.repository.TokenRepository;
import vn.eledevo.vksbe.repository.UserRepository;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationService {
    final UserRepository repository;
    final TokenRepository tokenRepository;
    final PasswordEncoder passwordEncoder;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;

    /**
     * Đăng ký người dùng mới vào hệ thống.
     *
     * @param request Đối tượng RegisterRequest chứa thông tin đăng ký
     * @return Đối tượng AuthenticationResponse chứa token truy cập và token làm mới
     */
    public AuthenticationResponse register(RegisterRequest request) {

        // Tạo đối tượng User mới từ thông tin đăng ký
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .identificationNumber(request.getIdentificationNumber())
                .dateOfBirth(request.getDateOfBirth())
                .status(request.getStatus())
                .role(Role.ADMIN)
                .build();

        // Lưu đối tượng User vào cơ sở dữ liệu
        var savedUser = repository.save(user);

        // Tạo token truy cập và token làm mới cho người dùng
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Lưu token truy cập vào cơ sở dữ liệu
        saveUserToken(savedUser, jwtToken);

        // Trả về đối tượng AuthenticationResponse chứa các token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Xác thực người dùng vào hệ thống.
     *
     * @param request Đối tượng AuthenticationRequest chứa thông tin xác thực
     * @return Đối tượng AuthenticationResponse chứa token truy cập và token làm mới
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws ValidationException {

        // Tìm đối tượng User tương ứng với email
        var user = repository.findByUsername(request.getUsername()).orElseThrow();

        if (user.getStatus() == UserStatus.LOCKED) {
            throw new ValidationException("lockedAccount", "Tài khoản của bạn đã bị khóa!");
        }

        if (user.getRole() == Role.USER) {
            throw new ValidationException("invalidRoleAccess", "Bạn không có quyền truy cập");
        }

        // Xác thực thông tin đăng nhập của người dùng
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        // Tạo token truy cập và token làm mới cho người dùng
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Hủy tất cả các token hiện có của người dùng
        revokeAllUserTokens(user);

        // Lưu token truy cập mới vào cơ sở dữ liệu
        saveUserToken(user, jwtToken);

        // Trả về đối tượng AuthenticationResponse chứa các token
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Lưu token truy cập cho người dùng vào cơ sở dữ liệu.
     *
     * @param user     Đối tượng User
     * @param jwtToken Token truy cập JWT
     */
    private void saveUserToken(User user, String jwtToken) {

        // Tạo đối tượng Token mới
        var token = Token.builder()
                .user(user)
                .accessToken(jwtToken)
                .tokenType(TokenType.BEARER)
                .isExpired(false)
                .isRevoked(false)
                .build();

        // Lưu đối tượng Token vào cơ sở dữ liệu
        tokenRepository.save(token);
    }

    /**
     * Hủy tất cả các token hiện có của người dùng.
     *
     * @param user Đối tượng User
     */
    private void revokeAllUserTokens(User user) {

        // Tìm tất cả các token hợp lệ của người dùng
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;

        // Đánh dấu các token đó là hết hạn và bị hủy
        validUserTokens.forEach(token -> {
            token.setIsExpired(true);
            token.setIsRevoked(true);
        });

        // Lưu các token đã cập nhật vào cơ sở dữ liệu
        tokenRepository.saveAll(validUserTokens);
    }

    /**
     * Làm mới token truy cập cho người dùng.
     *
     * @param request  Đối tượng HttpServletRequest
     * @param response Đối tượng HttpServletResponse
     * @throws IOException Ngoại lệ xảy ra khi ghi dữ liệu vào luồng đầu ra
     */
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {

        // Lấy token làm mới từ header yêu cầu
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String username;
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        refreshToken = authHeader.substring(7);

        // Trích xuất email người dùng từ token làm mới
        username = jwtService.extractUsername(refreshToken);
        if (username != null) {
            var user = this.repository.findByUsername(username).orElseThrow();
            // Kiểm tra tính hợp lệ của token làm mới
            if (jwtService.isTokenValid(refreshToken, user)) {
                // Nếu token làm mới hợp lệ:
                //   - Tạo token truy cập mới cho người dùng
                //   - Hủy tất cả các token hiện có của người dùng
                //   - Lưu token truy cập mới vào cơ sở dữ liệu
                //   - Trả về đối tượng AuthenticationResponse chứa các token mới
                var accessToken = jwtService.generateToken(user);
                revokeAllUserTokens(user);
                saveUserToken(user, accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();
                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }
}
