package com.edu.fud.projectfootbalpitch.config;

import com.edu.fud.projectfootbalpitch.dto.MyUser;
import com.edu.fud.projectfootbalpitch.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;


@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaAuditingConfig {
	@Autowired
	private UserRepository userRepository;
	@Bean
	public AuditorAware<String> auditorProvider() {
		return new AuditorAwareImpl();
	}
//	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//			if (authentication == null ||
//			!authentication.isAuthenticated()) {
//		return Optional.empty();
//	}
//
//	MyUser userPrincipal = (MyUser) authentication.getPrincipal();
//
//			return Optional.ofNullable(userPrincipal.getUsername());
	public static class AuditorAwareImpl implements AuditorAware<String> {

		@Override
		public Optional<String> getCurrentAuditor() {
            MyUser user;
            try {
                user = (MyUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                return Optional.of(user.getUsername());
            }catch (Exception e){
                return Optional.empty();
            }
		}
	}
}
