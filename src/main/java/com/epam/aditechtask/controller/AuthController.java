package com.epam.aditechtask.controller;

import com.epam.aditechtask.dto.UserDTO;
import com.epam.aditechtask.service.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authorization", description = "Authorization management APIs")
public class AuthController {

	private final AuthorizationService authorizationService;

	@PostMapping("/signup")
	@Operation(summary = "Sign Up", description = "Creates new user with default role")
	public ResponseEntity<Void> signup(@RequestBody final UserDTO user) {
		try {
			authorizationService.signUp(user.username(), user.password());
			return ResponseEntity.ok().build();
		} finally {
			Arrays.fill(user.password(), '\0');
		}
	}

	@PostMapping("/login")
	@Operation(summary = "Login", description = "Authenticates user and generates JWT token")
	public String login(@RequestBody final UserDTO user) {
		try {
			return authorizationService.login(user.username(), user.password());
		} finally {
			Arrays.fill(user.password(), '\0');
		}
	}
}
