package vn.eledevo.vksbe.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.repository.TokenRepository;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final TokenRepository tokenRepository;
    /**
     * Xử lý đăng xuất người dùng khỏi hệ thống.
     * Khi người dùng gửi yêu cầu đăng xuất, phương thức này sẽ được gọi.
     *
     * @param request        Đối tượng HttpServletRequest chứa thông tin về yêu cầu đăng xuất.
     * @param response       Đối tượng HttpServletResponse dùng để trả về phản hồi cho yêu cầu.
     * @param authentication Đối tượng Authentication chứa thông tin xác thực của người dùng.
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        final String authHeader =
                request.getHeader("Authorization"); // Lấy giá trị của header "Authorization" từ yêu cầu
        final String jwt;
        // Kiểm tra nếu header không tồn tại hoặc không bắt đầu bằng chuỗi "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return;
        }
        // Nếu header tồn tại và đúng định dạng, lấy chuỗi JWT từ sau cụm "Bearer "
        jwt = authHeader.substring(7);
        var storedToken = tokenRepository
                .findByAccessToken(jwt) // Tìm token trong cơ sở dữ liệu dựa trên chuỗi JWT
                .orElse(null);
        if (storedToken != null) { // Nếu token tồn tại trong cơ sở dữ liệu
            storedToken.setIsExpired(true); // Đánh dấu token đã hết hạn
            storedToken.setIsRevoked(true); // Đánh dấu token đã bị thu hồi
            tokenRepository.save(storedToken); // Lưu lại trạng thái mới của token vào cơ sở dữ liệu
            SecurityContextHolder.clearContext(); // Xóa thông tin xác thực khỏi SecurityContext
        }
    }
}
