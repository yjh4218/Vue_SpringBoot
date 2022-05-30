package com.vue_spring.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration // 빈등록(IOC관리)
@EnableWebSecurity // 시큐리티 필터 추가 = 스프링 시큐리티가 활성화가 되어 있는데 어떤 설정을 해당 파일에서 하겠다. 시큐리티 필터가 등록이 됨
@EnableGlobalMethodSecurity(prePostEnabled = true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    // 접속시 /auth 경로는 모두 접근 가능 그 이외는 인증 필요함
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // csrf 토큰 비활성화(테스트시 걸어두는게 좋음). 시큐리티는 csrf 토큰이 없으면 접근을 막음
                .authorizeRequests()
                .antMatchers("/", "/**", "/main", "/js/**", "/css/**", "/image/**", "/dummy/**") // "/auth/**"
                .permitAll()
                .anyRequest()
                .authenticated();
        // .and()
        // .formLogin()
        // .loginPage("/auth/loginForm") // 인증이 필요한 모든 요청은 loginForm으로 진행
        // .loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 요청오는 로그인을 가로채서 대신
        // 로그인 진행.
        // 로그인을 가로채서 PrincipalDetailService의 loadUserByUsername로 전달함.
        // .defaultSuccessUrl("/"); // 정상적으로 로그인시 홈페이지 이동
    }
}
