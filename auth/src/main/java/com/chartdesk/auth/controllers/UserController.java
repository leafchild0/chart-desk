package com.chartdesk.auth.controllers;

import com.chartdesk.auth.model.User;
import com.chartdesk.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * API to get infor about users
 *
 * @author vmalyshev
 * @date 22.02.2022
 */
@RestController
@Slf4j
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get user info by user id
     *
     * @param userId - id of the user
     * @return - found User
     */
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<User>> getUserById(@PathVariable String userId) {
        return Mono.just(userDetailsService.findByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    /**
     * Get list of users for admin user
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<User>>> getAllUsers() {

        return Mono.just(ResponseEntity.ok(userDetailsService.findAllUsers()));
    }
}
