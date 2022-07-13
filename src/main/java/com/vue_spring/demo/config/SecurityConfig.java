package com.vue_spring.demo.config;

import com.vue_spring.demo.jwt.JwtAccessDeniedHandler;
import com.vue_spring.demo.jwt.JwtAuthenticationEntryPoint;
import com.vue_spring.demo.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration // 빈등록(IOC관리)
@EnableWebSecurity // 시큐리티 필터 추가 = 스프링 시큐리티가 활성화가 되어 있는데 어떤 설정을 해당 파일에서 하겠다. 시큐리티 필터가 등록이 됨
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final TokenProvider tokenProvider;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // h2 database 테스트가 원활하도록 관련 API 들은 전부 무시
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers("/h2-console/**", "/favicon.ico");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable() // csrf 토큰 비활성화(테스트시 걸어두는게 좋음). 시큐리티는 csrf 토큰이 없으면 접근을 막음
                // exception handling 할 때 우리가 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                // 시큐리티는 기본적으로 세션을 사용
                // 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 로그인, 회원가입 API 는 토큰이 없는 상태에서 요청이 들어오기 때문에 permitAll 설정
                .and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/user/**").access("ROLE_USER")				// 사용자 페이지
                .antMatchers("/admin/**").access("ROLE_ADMIN")				// 관리자 페이지
                .antMatchers("/", "/**", "/main", "/js/**", "/css/**", "/image/**", "/dummy/**") // "/auth/**"
                .permitAll()
                .anyRequest().authenticated()   // 나머지 API 는 전부 인증 필요

                // JwtFilter 를 addFilterBefore 로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(tokenProvider));

//                .and()
//                .authorizeRequests()
//                .antMatchers("/", "/**", "/main", "/js/**", "/css/**", "/image/**", "/dummy/**") // "/auth/**"
//                .permitAll()
//                .anyRequest()
//                .authenticated();
        // .and()
        // .formLogin()
        // .loginPage("/auth/loginForm") // 인증이 필요한 모든 요청은 loginForm으로 진행
        // .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신
        // 로그인 진행.
        // 로그인을 가로채서 PrincipalDetailService의 loadUserByUsername로 전달함.
        // .defaultSuccessUrl("/"); // 정상적으로 로그인시 홈페이지 이동
    }
}
