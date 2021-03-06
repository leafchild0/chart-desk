package com.chartdesk.auth.controllers;

import com.chartdesk.auth.dto.UserDTO;
import com.chartdesk.auth.model.User;
import com.chartdesk.auth.service.CustomUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

/**
 * API to get info about users
 *
 * @author vmalyshev
 * @date 22.02.2022
 */
@RestController
@Slf4j
@RequestMapping
public class UserController {

    private final CustomUserDetailsService userDetailsService;

    public UserController(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Get user info by user id
     *
     * @return - found User
     */
    @GetMapping("/user")
    public Mono<ResponseEntity<UserDTO>> getUserById(Authentication authentication) {
        return userDetailsService.getByUsername(String.valueOf(authentication.getPrincipal()))
                .map(u -> ResponseEntity.ok(u.toDto()))
                .switchIfEmpty(Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    /**
     * Get list of users for admin user
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<List<UserDTO>>> getAllUsers() {
        return Mono.just(ResponseEntity.ok(
                userDetailsService.findAllUsers().stream()
                        .map(User::toDto)
                .collect(Collectors.toList())));
    }

    /**
     * Update user by id
     */
    @PutMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<UserDTO>> updateUser(@RequestBody UserDTO user) {
        return Mono.just(userDetailsService.updateUser(user)
                .map(u -> ResponseEntity.ok(u.toDto()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    /**
     * Disable user
     */
    @PostMapping("user/{userId}/disable")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<ResponseEntity<Object>> disableUser(@PathVariable String userId) {
        return Mono.just(userDetailsService.disableUser(Long.valueOf(userId))
                .map(u -> ResponseEntity.ok().build())
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}
