package com.dataan.config;

import com.dataan.filter.TokenAuthorizationFilter;
import com.dataan.vo.ErrorVo;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author zhan bing liang
 * @date 2024/6/3 16:36
 */
@Configuration
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,TokenAuthorizationFilter tokenAuthorizationFilter) throws Exception {
        http.authorizeHttpRequests()
            .antMatchers("/user/login").permitAll()
            .antMatchers(HttpMethod.POST,"/user").permitAll()
//            .antMatchers("/**").permitAll()
            .antMatchers("/error").permitAll()
            .antMatchers("/docs/**").permitAll()
            .antMatchers("/docs-api/**").permitAll()
            .antMatchers("/**/swagger-ui/**").permitAll()
            .anyRequest().access(tokenAuthorizationFilter)
        ;
        http.exceptionHandling().authenticationEntryPoint((request, response, authException) -> {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(new ErrorVo(HttpStatus.UNAUTHORIZED.value(), authException.getMessage(), LocalDateTime.now(), request.getRequestURL().toString()).json());
        }).accessDeniedHandler((request, response, accessDeniedException) -> {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            response.getWriter().write(new ErrorVo(HttpStatus.FORBIDDEN.value(), accessDeniedException.getMessage(), LocalDateTime.now(), request.getRequestURL().toString()).json());
        });
        //关闭session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        //关闭csrf,因为已经用无状态JWT了就无需开启CSRF防护
        http.csrf().disable();

        //返回
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {


        return new BCryptPasswordEncoder();
    }

}
