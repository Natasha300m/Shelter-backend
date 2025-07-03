package com.shelter.mykyda.security.config;

import com.shelter.mykyda.security.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;
    private final JwtFilter jwtFilter;
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AccessDeniedHandler accessDeniedHandler;

    @Bean
    public SecurityFilterChain getFilterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(e ->
                e.authenticationEntryPoint(authenticationEntryPoint).accessDeniedHandler(accessDeniedHandler));

        http.formLogin(AbstractHttpConfigurer::disable);

        http.logout(e -> e.logoutUrl("/logout").
                deleteCookies("accessToken", "JSESSIONID").
                logoutRequestMatcher(new AntPathRequestMatcher("/logout", HttpMethod.GET.toString())).
                logoutSuccessHandler((request, response, authentication) -> {
                    response.setStatus(HttpServletResponse.SC_OK);
                    response.getWriter().write("Logout successful");
                }));

        http.userDetailsService(userService);

        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        http.authorizeHttpRequests(request -> request.
                        requestMatchers("/login", "/registration", "/csrf", "/chat/**", "/ws/info", "/stream-flux", "/favicon.ico", "/api/**").permitAll().
                        //requestMatchers(HttpMethod.POST, "/api/posts", "/api/newsItems". "/api/shelters").authenticated().
                        //requestMatchers(HttpMethod.PUT, "/api/posts/**", "/api/newsItems/**").authenticated().
                        //requestMatchers(HttpMethod.DELETE, "/api/posts/**", "/api/newsItems/**","/api/shelters").authenticated().
                        //requestMatchers(HttpMethod.GET, "/api/posts", "/api/posts/**", "/api/newsItems", "/api/newsItems/**").permitAll().
//                requestMatchers("/api/**", "/currentAccount").authenticated().
//                requestMatchers("/admin/**").hasAuthority(Role.VOLUNTEER.getAuthority()).
        anyRequest().authenticated()
        );
        return http.build();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOriginPatterns("*")
                        .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .exposedHeaders("Set-Cookie")
                        .allowCredentials(true);
            }
        };
    }
}

