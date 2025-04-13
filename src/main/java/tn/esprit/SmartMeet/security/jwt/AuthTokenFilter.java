package tn.esprit.SmartMeet.security.jwt;

import java.io.IOException;
import java.util.Optional;

import tn.esprit.SmartMeet.models.BlacklistedToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import tn.esprit.SmartMeet.Repositories.BlacklistedTokenRepository;
import tn.esprit.SmartMeet.security.UserServices.UserDetailsServiceImpl;

public class AuthTokenFilter extends OncePerRequestFilter {

  private final JwtUtils jwtUtils;
  private final UserDetailsServiceImpl userDetailsService;
  private final BlacklistedTokenRepository blacklistedTokenRepository;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  // ✅ Utilisation du constructeur pour injecter les dépendances (meilleure pratique)

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
          throws ServletException, IOException {
    try {
      String jwt = parseJwt(request);

      if (jwt != null) {
        // ✅ Vérifier si le token est blacklisté (déconnecté)
        Optional<BlacklistedToken> blacklisted = blacklistedTokenRepository.findByToken(jwt);
        if (blacklisted.isPresent()) {
          logger.warn("Token refusé car en liste noire.");
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré.");
          return;
        }        if (blacklisted.isPresent()) {
          logger.warn("Token refusé car en liste noire.");
          response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou expiré.");
          return;
        }

        // ✅ Vérification et authentification de l'utilisateur
        if (jwtUtils.validateJwtToken(jwt)) {
          String username = jwtUtils.getUserNameFromJwtToken(jwt);

          UserDetails userDetails = userDetailsService.loadUserByUsername(username);
          UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                  userDetails, null, userDetails.getAuthorities());
          authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

          SecurityContextHolder.getContext().setAuthentication(authentication);
        }
      }
    } catch (Exception e) {
      logger.error("Erreur lors de l'authentification : {}", e.getMessage());
    }

    filterChain.doFilter(request, response);
  }

  private String parseJwt(HttpServletRequest request) {
    String headerAuth = request.getHeader("Authorization");

    if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
      return headerAuth.substring(7);
    }

    return null;
  }
  @Autowired

  public AuthTokenFilter(JwtUtils jwtUtils, UserDetailsServiceImpl userDetailsService, BlacklistedTokenRepository blacklistedTokenRepository) {
    this.jwtUtils = jwtUtils;
    this.userDetailsService = userDetailsService;
    this.blacklistedTokenRepository = blacklistedTokenRepository;
  }

}
