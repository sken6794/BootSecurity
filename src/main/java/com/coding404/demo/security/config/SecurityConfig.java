package com.coding404.demo.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.coding404.demo.user.MyUserDetailService;

@Configuration // 설정파일
@EnableWebSecurity // 이 설정파일을 시큐리티 필터에 추가
@EnableGlobalMethodSecurity(prePostEnabled = true) //어노테이션으로 권한을 지정할 수 있게 함
public class SecurityConfig {

	
	//rememberMe 에서 사용할 UserDetailService
	@Autowired
	private MyUserDetailService myUserDetailService;
	
	
	// 비밀번호 암호화객체
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain securityFilter(HttpSecurity http) throws Exception {

		// csrf 토큰 x
		http.csrf().disable();

		// 권한 설정
		// http.authorizeHttpRequests(authorize -> authorize.anyRequest().permitAll());

		// 모든 페이지에 대해서 거부
		// http.authorizeHttpRequests(authorize -> authorize.anyRequest().denyAll());

		// user페이지에 대해서 인증이 필요
		/*
		 * http.authorizeHttpRequests(authorize -> authorize.antMatchers("/user/**")
		 * .authenticated());
		 */

		// user페이지에 대해서 권한이 필요
		// http.authorizeHttpRequests(authorize ->
		// authorize.antMatchers("/user/**").hasRole("USER"));

		// user페이지는 user권한이 필요, admin페이지는 admin권한이 필요
		// http.authorizeHttpRequests(authorize ->
		// authorize.antMatchers("/user/**").hasRole("USER")
		// .antMatchers("/admin/**").hasRole("ADMIN"));

		// all 페이지는 인증만 되면 됨, user페이지에 대해서 USER권한이 필요, admin페이지에 ADMIN권한 필요
		// 나머지 모든 페이지는 접근이 가능.
		/*
		 * http.authorizeHttpRequests(authorize ->
		 * authorize.antMatchers("/all").authenticated()
		 * .antMatchers("/user/**").hasRole("USER")
		 * .antMatchers("/admin/**").hasRole("ADMIN") .anyRequest().permitAll());
		 */

		// all 페이지는 인증만 되면 됨. user페이지는 적혀있는 것들 중 1개의 권한만 가지면 접근 가능
		// 시큐리티는 권한을 설정하는 명칭이 정해져있다 ROLE_ 로 시작을하지만 자동으로 붙여준다
		http.authorizeHttpRequests(authorize->authorize.antMatchers("/all").authenticated()
                .antMatchers("/user/**").hasAnyRole("USER","ADMIN","TESTER")
                .antMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().permitAll());

		// 시큐리티 설정파일 만들면, 시큐리티가 제공하는 기본 로그인페이지가 보이지 않게 된다.
		// 시큐리티가 사용하는 기본 로그인페이지를 사용함
		// 권한 or 인증이 되지 않으면 기본으로 선언된 로그잉ㄴ 페이지를 보여주게 된다.
		// http.formLogin(Customizer.withDefaults()); //기본 로그인 페이지 사용

		// 사용자가 제공하는 폼 기반 로그인 기능을 사용할 수 있습니다.
		http.formLogin()
			.loginPage("/login") //로그인화면
			.loginProcessingUrl("/loginForm")//로그인시도 할 때 요청 경로 -> 스프링이 로그인 시도를 가로채서 UserDetailService 객체로 연결
			.defaultSuccessUrl("/all") //로그인 성공시 페이지 
			.failureUrl("/login?err=true") //로그인 실패시 이동할 url
			.and()
			.exceptionHandling().accessDeniedPage("/deny") //권한이 없을 때 이동할 리다이렉트 경로
			.and()
			//logout 주소를 직접 작성할 수 있고, 로그아웃 성공시 리다이렉트 할 경로도 logoutSuccessUrl("/hello") 로 설정가능 
			.logout().logoutUrl("/logout").logoutSuccessUrl("/hello"); //default로그아웃 경로 /logout 이다.
			
		//rememberMe
		http.rememberMe()
			.key("coding404") //토큰 (쿠키) 를 만들 비밀 키 (필수)
			.rememberMeParameter("remember-me") //화면에서 전달받는 checked name명 (필수)
			.tokenValiditySeconds(60) //쿠키(토큰)의 유효시간 (필수)
			.userDetailsService(myUserDetailService) //토근이 있을 때 실행시킬 userDetailService 객체 (필수)
			.authenticationSuccessHandler(customRememberMe()); //rememberMe가 동작할 때, 실행할 핸들러 객체
		
		
		
		return http.build();
	}
	
	
	//customRememberMe의 빈 등록하는 방법. autowired로 주입 안됨 이렇게 빈 등록 해야함 
	@Bean
	public CustomRememberMe customRememberMe() {
		//생성자 만들 때 매개변수 하나 받게 만들고 해당 매개변수가 필드 값으로 받게끔 해놨음
		CustomRememberMe me = new CustomRememberMe("/all"); //rememberMe 성공시 실행시킬 리다이렉트 주소가 된다. 
		return me;
	}
	
	
}
