package latihan.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsService userDetailService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()

                .antMatchers( "/create_user_form.zul", "/users.zul", "/user_detail.zul", "/update_user.zul")
                .hasAnyRole("ADMIN")

                .antMatchers("/dashboard.zul", "/ticket_detail.zul", "/ticket_form.zul", "/tickets.zul")
                .hasAnyRole("ADMIN", "AGENT", "USER")
                .and()
                .formLogin()
                .loginPage("/login.zul")
                .defaultSuccessUrl("/dashboard.zul")
                .and()
                .logout()
                .logoutUrl("/logout")
                .permitAll()
                .and().exceptionHandling().accessDeniedPage("/access_denied.zul");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}