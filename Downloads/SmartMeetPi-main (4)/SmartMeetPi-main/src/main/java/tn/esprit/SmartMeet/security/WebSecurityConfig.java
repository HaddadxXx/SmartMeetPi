package tn.esprit.SmartMeet.security;

import org.springframework.http.HttpMethod;
import tn.esprit.SmartMeet.DAO.Repositories.BlacklistedTokenRepository;
import tn.esprit.SmartMeet.security.jwt.JwtUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
//import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import tn.esprit.SmartMeet.security.jwt.AuthEntryPointJwt;
import tn.esprit.SmartMeet.security.jwt.AuthTokenFilter;
import tn.esprit.SmartMeet.Services.UserServices.UserDetailsServiceImpl;

import java.util.List;

@Configuration

//@EnableWebSecurity
@EnableMethodSecurity

//(securedEnabled = true,
//jsr250Enabled = true,
//prePostEnabled = true) // by default

public class WebSecurityConfig {

  private final JwtUtils jwtUtils;

  private final UserDetailsServiceImpl userDetailsService;
  private final BlacklistedTokenRepository blacklistedTokenRepository;
  private final AuthEntryPointJwt unauthorizedHandler;

  public WebSecurityConfig(JwtUtils jwtUtils,
                           UserDetailsServiceImpl userDetailsService,
                           BlacklistedTokenRepository blacklistedTokenRepository,
                           AuthEntryPointJwt unauthorizedHandler) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
    this.blacklistedTokenRepository = blacklistedTokenRepository;
    this.unauthorizedHandler = unauthorizedHandler;
  }

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(jwtUtils, userDetailsService, blacklistedTokenRepository);
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
    authProvider.setUserDetailsService(userDetailsService);
    authProvider.setPasswordEncoder(passwordEncoder());
    return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/api/auth/send-otp").permitAll()
                    .requestMatchers( "/api/auth/verify-otp").permitAll()// ✅ Autoriser OTP sans auth
                    .requestMatchers("/api/auth/**").permitAll()
                    .requestMatchers("/api/test/**").permitAll()
                    .requestMatchers("/api/uploads/**").permitAll()
                    .requestMatchers("/api/uploads/profilimages/**").permitAll()
                    .requestMatchers("/uploads/profilimages/**").permitAll()
                    .requestMatchers("/api/groups/**").permitAll()
                    .requestMatchers("/api/offers/**").permitAll()
                    .requestMatchers("/api/events/**").permitAll()
                    .requestMatchers("/api/contracts/**").permitAll()
                    .requestMatchers("/api/requests/**").permitAll()

                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()



                    .requestMatchers("/api/users/me").permitAll()
                    .requestMatchers("/api/users/search").permitAll()
                    .requestMatchers("/api/friend-requests/**").permitAll() // Ajout pour tester friend requests
                    .requestMatchers("/api/friends/**").permitAll()          // Ajout pour tester la liste d'amis
                    .requestMatchers(HttpMethod.POST, "/api/groups/create").authenticated()
                    //.requestMatchers("/api/groups/**").permitAll()
                    .requestMatchers("/api/test/protected").authenticated()
                    .anyRequest().authenticated());
    http.cors(cors -> cors.configurationSource(request -> {
      org.springframework.web.cors.CorsConfiguration config = new org.springframework.web.cors.CorsConfiguration();
      config.setAllowCredentials(true); // Permet les cookies 🔥
      config.setAllowedOrigins(List.of("http://localhost:4200")); // Adresse Frontend
      config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
      config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
      config.setExposedHeaders(List.of("Authorization")); // 🔥 Exposer le JWT

      return config;
    }));


    http.authenticationProvider(authenticationProvider());

    // ✅ Assurer que le filtre JWT est appliqué avant UsernamePasswordAuthenticationFilter
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }

}
