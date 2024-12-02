package MedMap.service;

import MedMap.model.User;
import MedMap.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String jwtSecret = "7qF0Yq6IBvKOcsmQI7PYZfCXlL50zi2vV9514w9CkBW5dWZx2oxDZqM2m98SiDH1h5ZfUvjyDtg2r7c12POPSg"; // Substitua pelo seu Base64 Secret
    private final long jwtExpiration = 3600L * 1000; // 1 hora em milissegundos

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
        var secretKey = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .setSubject(user.getNomeUbs())
                .claim("cnes", user.getCnes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(secretKey)
                .compact();
    }
}
