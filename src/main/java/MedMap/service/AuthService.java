package MedMap.service;

import MedMap.model.User;
import MedMap.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private int jwtExpiration;

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String register(User user) {
        // Validações adicionais para evitar salvar dados incompletos
        if (user.getNomeUbs() == null || user.getNomeUbs().isBlank()) {
            throw new RuntimeException("O nome da UBS é obrigatório.");
        }
        if (user.getCnes() == null || user.getCnes().isBlank()) {
            throw new RuntimeException("O CNES é obrigatório.");
        }
        if (user.getAddress() == null || user.getAddress().isBlank()) {
            throw new RuntimeException("O endereço é obrigatório.");
        }

        if (userRepository.findByNomeUbsAndCnes(user.getNomeUbs(), user.getCnes()).isPresent()) {
            throw new RuntimeException("A UBS com esse nome e CNES já está registrada.");
        }

        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    public String login(String nomeUbs, String cnes) {
        User user = userRepository.findByNomeUbsAndCnes(nomeUbs, cnes)
                .orElseThrow(() -> new RuntimeException("UBS não encontrada ou dados inválidos"));

        return generateToken(user);
    }

    private String generateToken(User user) {
        byte[] keyBytes = Base64.getDecoder().decode(jwtSecret); // Decodifica a chave Base64
        var secretKey = Keys.hmacShaKeyFor(keyBytes); // Gera a chave para HS512

        return Jwts.builder()
                .setSubject(user.getNomeUbs())
                .claim("cnes", user.getCnes())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration * 1000L))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }
}