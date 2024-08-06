package vn.eledevo.vksbe.config.security;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.RequiredArgsConstructor;
import vn.eledevo.vksbe.config.security.auditing.ApplicationAuditAware;
import vn.eledevo.vksbe.repository.UserRepository;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {
    private final UserRepository repository;

    /**
     * Cấu hình bean UserDetailsService để lấy thông tin người dùng từ cơ sở dữ liệu.
     *
     * @return Đối tượng UserDetailsService tùy chỉnh
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> (UserDetails)
                repository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
    /**
     * Cấu hình bean AuthenticationProvider để xác thực người dùng.
     *
     * @return Đối tượng AuthenticationProvider
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    /**
     * Cấu hình bean AuditorAware để cung cấp thông tin về người dùng hiện tại cho mục đích kiểm toán.
     *
     * @return Đối tượng AuditorAware
     */
    @Bean
    public AuditorAware<UUID> auditorAware() {
        return new ApplicationAuditAware();
    }
    /**
     * Cấu hình bean AuthenticationManager để quản lý xác thực.
     *
     * @param config Đối tượng AuthenticationConfiguration
     * @return Đối tượng AuthenticationManager
     * @throws Exception Ngoại lệ xảy ra nếu cấu hình không hợp lệ
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
    /**
     * Cấu hình bean PasswordEncoder để mã hóa mật khẩu.
     *
     * @return Đối tượng PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
