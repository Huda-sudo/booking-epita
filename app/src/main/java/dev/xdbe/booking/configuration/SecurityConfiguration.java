package dev.xdbe.booking.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    
    @Bean
    public UserDetailsService users() {
        UserDetails admin = User.builder()
            .username("admin")
            //.password("{bcrypt}$2a$10$WDafrGvYCVxRTZlvDxenoe9qc0l9teedBQ1vlzJDnsTa.TVwQcaXC")
            //.password("admin123")
            .password("{bcrypt}$2a$10$P7zKcZVpPG/hm8S2ZDLU0uSc6KNweqVUoGfAyUNNSTkrQiktgV98W")
            .roles("ADMIN")
            .build();
        
        UserDetails user = User.builder()
            .username("user")
            .password("user123")
            .roles("USER")
            .build();
        
        return new InMemoryUserDetailsManager(admin, user);
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf
                .ignoringRequestMatchers("/h2-console/**")
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/dashboard").hasRole("ADMIN")
                //.requestMatchers("/booking").permitAll()
                .anyRequest().permitAll()
            )
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            )
            .formLogin(withDefaults())
            .logout(withDefaults());
            
        return http.build();
    }
}
