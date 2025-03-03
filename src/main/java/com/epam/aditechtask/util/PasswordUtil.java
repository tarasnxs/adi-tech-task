package com.epam.aditechtask.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class PasswordUtil {

	private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public static String hashPassword(char[] password) {
		return passwordEncoder.encode(new String(password));
	}

	public static boolean checkPassword(char[] rawPassword, String hashedPassword) {
		return passwordEncoder.matches(new String(rawPassword), hashedPassword);
	}
}
