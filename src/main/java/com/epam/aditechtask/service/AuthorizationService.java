package com.epam.aditechtask.service;

import com.epam.aditechtask.exception.ResourceAlreadyExistException;
import com.epam.aditechtask.persistence.entity.Role;
import com.epam.aditechtask.persistence.entity.UserEntity;
import com.epam.aditechtask.persistence.repository.UserRepository;
import com.epam.aditechtask.util.JwtUtil;
import com.epam.aditechtask.util.PasswordUtil;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public String login(String username, char[] password) {
		try {
			Optional<UserEntity> user = userRepository.findByName(username);
			if (user.isPresent() && PasswordUtil.checkPassword(password, user.get().getPassword())) {
				return jwtUtil.generateToken(username, user.get().getRoles());
			}
			throw new BadCredentialsException("Invalid username or password");
		} finally {
			Arrays.fill(password, '\0');
		}
	}

	public void signUp(String username, char[] password) {
		try {
			userRepository.findByName(username).ifPresent(user -> {
				throw new ResourceAlreadyExistException("User with username '" + username + "' already exists");
			});
			UserEntity user = new UserEntity();
			user.setName(username);
			user.setPassword(PasswordUtil.hashPassword(password));
			user.setRoles(Set.of(Role.USER));
			userRepository.save(user);
		} finally {
			Arrays.fill(password, '\0');
		}
	}
}
