package com.chartdesk.auth.controllers;

import com.chartdesk.auth.dto.LoginDTO;
import com.chartdesk.auth.dto.SignUpDTO;
import com.chartdesk.auth.dto.UserDTO;
import com.chartdesk.auth.jwt.JwtTokenUtil;
import com.chartdesk.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

/**
 * Auth API, login and register
 *
 * @author vmalyshev
 * @date 22.02.2022
 */
@RestController
@Slf4j
public class AuthController {

    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public AuthController(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * Logins user and generate token
     *
     * @param loginDTO - login details
     * @return - token as string
     */
    @PostMapping("/login")
    public Mono<ResponseEntity<String>> login(@RequestBody LoginDTO loginDTO) {

	    return userDetailsService.findByUsername(loginDTO.getUsername())
		    .map(userDetails -> {
			    if (!passwordEncoder.matches(loginDTO.getPassword(), userDetails.getPassword())) {
				    throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Wrong username or password");
			    }
				else {
				    return ResponseEntity.ok(jwtTokenUtil.generateToken(userDetails));
			    }
		    })
		    .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    /**
     * Register up user
     *
     * @param signUpDTO - login details
     * @return - token as string
     */
    @PostMapping("/register")
    public Mono<ResponseEntity<UserDTO>> register(@RequestBody SignUpDTO signUpDTO) {

        return userDetailsService.createNewUser(signUpDTO).map(user -> ResponseEntity.ok(user.toDto()));
    }
}
