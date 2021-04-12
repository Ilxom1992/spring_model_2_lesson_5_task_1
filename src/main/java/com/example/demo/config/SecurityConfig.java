package com.example.demo.config;

import com.example.demo.security.JwtFilter;
import com.example.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.Properties;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

 final AuthService authService;
 final JwtFilter jwtFilter;

    public SecurityConfig(@Lazy AuthService authService,@Lazy JwtFilter jwtFilter) {
        this.authService = authService;
        this.jwtFilter = jwtFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(authService).passwordEncoder(passwordEncoder());
    }

    /**
     * bu yerda tizimdagi huquqlar belgilangan ochi yopiq yollar berilgan
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {

       http
               .csrf().disable()
               .authorizeRequests()
               .antMatchers("/api/auth/register","/api/auth/verifyEmail","/api/auth/login","/api/card/**")
               .permitAll()
               .anyRequest()
               .authenticated()
               .and()
               .httpBasic();
http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    /**
     * gmail.com ga hat yuborish uchun ishlatiladi
     * nastroykalari
     * @return
     */
    @Bean
    public JavaMailSender javaMailSender(){
    JavaMailSenderImpl mailSender=new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);
    mailSender.setUsername("ilxom.xojamurodov@gmail.com");
    mailSender.setPassword("");
    Properties properties =mailSender.getJavaMailProperties();
    properties.put("mail.transport.protocol","smtp");
    properties.put("mail.smtp.auth","true");
    properties.put("mail.smtp.starttls.enable","true");
    properties.put("mail.debug","true");
    return  mailSender;
}

    /**
     * passwordni coddlab beradi encod qiladi
     * @return
     */
    @Bean
PasswordEncoder passwordEncoder(){
        return new  BCryptPasswordEncoder();
}

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


}
