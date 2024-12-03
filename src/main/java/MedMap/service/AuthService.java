package MedMap.service;

import MedMap.model.User;
import MedMap.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String jwtSecret;
    private final long jwtExpiration;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       @Value("${jwt.secret}") String jwtSecret,
                       @Value("${jwt.expiration:3600000}") long jwtExpiration) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtSecret = jwtSecret;
        this.jwtExpiration = jwtExpiration;
    }

    public String register(User user) {
        if (userRepository.findByNomeUbsAndCnes(user.getNomeUbs(), user.getCnes()).isPresent()) {
            throw new RuntimeException("A UBS com esse nome e CNES já está registrada.");
        }
        user.setAddress(passwordEncoder.encode(user.getAddress()));
        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    public String login(String nomeUbs, String cnes) {
        User user = userRepository.findByNomeUbsAndCnes(nomeUbs, cnes)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada ou dados inválidos"));

        return generateToken(user);
    }

    private String generateToken(User user) {
        try {
            // Decodifica a chave Base64 e valida o tamanho
            byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
            if (keyBytes.length < 32) { // Verifica se a chave tem pelo menos 256 bits (32 bytes)
                throw new IllegalArgumentException("JWT secret key must be at least 256 bits (32 bytes)");
            }
            var secretKey = Keys.hmacShaKeyFor(keyBytes);

            return Jwts.builder()
                    .setSubject(user.getNomeUbs())
                    .claim("cnes", user.getCnes())
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                    .signWith(secretKey)
                    .compact();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT secret is invalid or improperly formatted", e);
        }
    }
}
