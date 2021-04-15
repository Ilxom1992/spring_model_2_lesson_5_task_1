package com.example.demo.config;

import com.example.demo.entity.enams.RoleEnum;
import com.example.demo.model.AUTHORITY;
import com.example.demo.security.JwtFilter;
import com.example.demo.service.AuthService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
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
                .antMatchers("/auth/register","/auth/verifyEmail","/auth/login")
                .permitAll()
                .and()
                .httpBasic();
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
