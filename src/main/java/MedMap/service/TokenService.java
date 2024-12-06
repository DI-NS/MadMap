package MedMap.service;

import MedMap.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

/**
 * Serviço responsável por gerar tokens JWT.
 * Inclui nomeUbs e cnes como Claims no token.
 */
@Service
public class TokenService {

    private final JwtSecretProvider jwtSecretProvider;
    private final long jwtExpiration;

    public TokenService(JwtSecretProvider jwtSecretProvider, @Value("${jwt.expiration:3600000}") long jwtExpiration) {
        this.jwtSecretProvider = jwtSecretProvider;
        this.jwtExpiration = jwtExpiration;
    }

    /**
     * Gera um token JWT para a UBS autenticada.
     *
     * @param user Dados da UBS.
     * @return Token JWT contendo cnes e nomeUbs.
     */
    public String generateToken(User user) {
        String jwtSecret = jwtSecretProvider.getJwtSecret();
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        var secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(user.getCnes()) // CNES como subject
                .claim("nomeUbs", user.getNomeUbs()) // adicionar nomeUbs no token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }
}
