package vn.vano.cms.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import vn.yotel.commons.web.security.CustomAuthenticationProvider;
import vn.yotel.commons.web.security.MyPermissionEvaluator;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true, prePostEnabled = true, proxyTargetClass = true)
//@Profile("HRYotel")
public class WebSecurityDefaultConfiguration extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsService authUserDetailsService;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Bean("userLoginSuccessHandler")
	public AuthenticationSuccessHandler userLoginSuccessHandler() {
		return new AppAuthenticationSuccessHandler();
	}

	@Bean("customAuthenticationProvider")
	public AuthenticationProvider customAuthenticationProvider() {
		return new CustomAuthenticationProvider();
	}

	@Bean("myPermissionEvaluator")
	public PermissionEvaluator myPermissionEvaluator() {
		return new MyPermissionEvaluator();
	}

	/**
	 * global config for all http
	 */
    @Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(this.authUserDetailsService).passwordEncoder(this.passwordEncoder);
	}

    @Override
    public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**", "/resources/**", "/static/**", "/repository/**", "/assets/**",
				"/fonts/**", "/ws/**", "/v1/**");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .authorizeRequests()
                .antMatchers("/webjars/**", "/resources/**", "/assets/**", "/fonts/**", "/ws/**").permitAll()
                .antMatchers("/**")
				.hasAnyAuthority("Administrators", "Users", "SADMIN", "ADMIN", "REPORT", "CSKH", "TOPUP_CSKH", "TOPUP_ADMIN")
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error/403.html?authorization_error=true") //khi người dùng không đủ quyền truy cập thì sẽ redirect đến trang này
                .and()
//            .csrf().disable()
            .headers().frameOptions().disable().and()
        	.formLogin()
				.usernameParameter("j_username")  // tài khoản
				.passwordParameter("j_password") // mật khẩu
        		.loginPage("/login.html")
        		.failureUrl("/login.html?authentication_error=true") //đường dẫn tới trang đăng nhập thất bại
        		.loginProcessingUrl("/login.html") //Trong Spring Security, trang xử lý submit form mặc định là /login. Nếu bạn muốn custom thì có thể dùng loginProcessingUrl().
        		.successHandler(userLoginSuccessHandler())
        		.permitAll()
//        		.failureHandler(userLoginFailureHandler)
        	.and()
        	.logout()
               .logoutSuccessUrl("/login.html?logout=true")
               .logoutUrl("/logout.html")
               .logoutRequestMatcher(new AntPathRequestMatcher("/logout.html")) // for POST and GET
               .deleteCookies( "JSESSIONID" )
               .invalidateHttpSession(true)
               .permitAll()
             .and()
             	.sessionManagement()
	        		.invalidSessionUrl("/login.html?invalid=true") //?invalid
	        		.maximumSessions(-1) // -1  unlimit
	        		.expiredUrl("/login.html?expired=true")
	        		.maxSessionsPreventsLogin(false)// kho cho dang nhap neu da ton tai session
        	.and()
        		.enableSessionUrlRewriting(false)
        	;
        // @formatter:on
    }

    // define beans for remember-me func
    String applicationKey = "application.key";
}
