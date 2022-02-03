package com.chartdesk.gateway;

import javax.servlet.http.HttpServletResponse;

import com.chartdesk.gateway.security.JwtTokenAuthenticationFilter;
import com.chartdesk.gateway.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security config for the gateway
 *
 * @author vmalyshev
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private JwtTokenProvider tokenProvider;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {

        httpSecurity
                .csrf().disable()
                .logout().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .anonymous()
                .and()
                // Decided to not add error-handling here, but instead each service will do it
                .exceptionHandling().authenticationEntryPoint(
                        (req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()
                .addFilterAfter(new JwtTokenAuthenticationFilter(tokenProvider),
                        UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                // Allow login and register
                .antMatchers("/auth/login", "/auth/register").permitAll()
                // Allow all for actuator
                .antMatchers("/actuator/**").permitAll()
                .anyRequest()
                .authenticated();
    }
}

