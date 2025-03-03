package com.epam.aditechtask.config.filter;

import com.epam.aditechtask.persistence.entity.Role;
import com.epam.aditechtask.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collection;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
		throws ServletException, IOException {
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		String token = header.substring(7);
		if (jwtUtil.validateToken(token)) {
			String username = jwtUtil.extractUsername(token);
			Set<Role> roles = jwtUtil.extractRoles(token);

			UserDetails userDetails = new User(username, "", roles.stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
				.collect(Collectors.toSet()));

			var authentication = new JwtAuthenticationToken(userDetails, token, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}

		chain.doFilter(request, response);
	}

	public static class JwtAuthenticationToken extends AbstractAuthenticationToken {

		private final UserDetails principal;
		private final String token;

		public JwtAuthenticationToken(UserDetails principal, String token, Collection<? extends GrantedAuthority> authorities) {
			super(authorities);
			this.principal = principal;
			this.token = token;
			setAuthenticated(true);
		}

		@Override
		public Object getCredentials() {
			return token;
		}

		@Override
		public Object getPrincipal() {
			return principal;
		}
	}
}
