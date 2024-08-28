package vn.eledevo.vksbe.config.security;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;
import static vn.eledevo.vksbe.constant.Role.ADMIN;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.web.bind.annotation.CrossOrigin;

import lombok.RequiredArgsConstructor;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@CrossOrigin(origins = "http://localhost:4200")
public class SecurityConfiguration {

    private static final String[] WHITE_LIST_URL = {
        "/api/v1/auth/**",
        "/v2/api-docs",
        "/v3/api-docs",
        "/v3/api-docs/**",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui/**",
        "/webjars/**",
        "/swagger-ui.html",
        "/admin/quanlyuser",
        "admin/quanlyuser/search**",
        "admin/quanlyuser/filter**",
        "/admin/quanlykieuphong",
        "/admin/quanlykieuphong/search**",
        "/admin/quanlykieuphong/filter**",
        "admin/quanlyphong",
        "admin/quanlyphong/search**",
        "admin/quanlyphong/filter**",
        "admin/quanlydatphong/add",
        "admin/quanlydatphong/search**",
        "admin/quanlydatphong/filter**",
    };

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(req -> req.requestMatchers(WHITE_LIST_URL)
                        .permitAll()
                        .requestMatchers("/api/v1/private/**", "/api/v1/public/**")
                        .hasAnyRole(ADMIN.name())
                        .requestMatchers(
                                "/admin/quanlyuser/add",
                                "/admin/quanlyuser/update/**",
                                "/admin/quanlyuser/delete/**",
                                "/admin/quanlykieuphong/add",
                                "/admin/quanlykieuphong/update/**",
                                "/admin/quanlykieuphong/delete/**",
                                "/admin/quanlyphong/add",
                                "/admin/quanlyphong/update/**",
                                "/admin/quanlyphong/lock/**",
                                "/admin/quanlydatphong/update/**",
                                "/admin/quanlydatphong/cancel/**")
                        .hasRole(ADMIN.name())
                        .anyRequest()
                        .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider) // Để xác thực người dùng trong lần đăng nhập
                .addFilterBefore(
                        jwtAuthFilter,
                        UsernamePasswordAuthenticationFilter
                                .class) // Thêm bộ lọc xác thực JWT (Mỗi request đều được điều hướng đến doFilter để
                // kiểm tra)
                .logout(
                        logout -> // Cấu hình đăng xuất
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler) // Thêm xử lý đăng xuất
                                .logoutSuccessHandler((request, response, authentication) ->
                                        SecurityContextHolder
                                                .clearContext()) // Định nghĩa là đăng xuất thành công và xóa bỏ thông
                        // tin người dùng trong phiên đăng nhập trong
                        // SecurityContextHolder
                        );
        return http.build();
    }

    // Cấu hình CORS cho phép từ http://localhost:4200
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://localhost:4200"); // Cấu hình nguồn gốc (Origin) được phép
        configuration.addAllowedMethod("*"); // Cho phép tất cả các phương thức HTTP
        configuration.addAllowedHeader("*"); // Cho phép tất cả các header
        configuration.setAllowCredentials(true); // Cho phép gửi cookie hoặc các thông tin đăng nhập

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Áp dụng cấu hình này cho tất cả các URL

        return source;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
