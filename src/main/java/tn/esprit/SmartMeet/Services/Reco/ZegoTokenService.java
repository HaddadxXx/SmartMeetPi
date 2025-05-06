package tn.esprit.SmartMeet.Services.Reco;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class ZegoTokenService {

    @Value("${zego.appId}")
    private long appId;

    @Value("${zego.serverSecret}")
    private String serverSecret;

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        // Initialise la clé secrète à partir de la chaîne
        secretKey = Keys.hmacShaKeyFor(serverSecret.getBytes());
    }

    /**
     * Génère un token JWT Zego pour un utilisateur qui rejoint une room.
     * @param roomId Identifiant unique de la room (String/UUID)
     * @param userId Identifiant unique de l’utilisateur (String ou numérique)
     * @return le token JWT à passer au SDK client
     */
    public String generateToken(String roomId, String userId) {
        long now = System.currentTimeMillis();
        long expireMillis = now + 2 * 60 * 60 * 1000; // token valide 2 h

        return Jwts.builder()
                // En-tête : algorithm / type
                .setHeaderParam("alg", "HS256")
                .setHeaderParam("typ", "JWT")
                // Corps (claims)
                .claim("app_id", appId)
                .claim("room_id", roomId)
                .claim("user_id", userId)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(expireMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}
