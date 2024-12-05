package MedMap.service;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class JwtSecretProvider {

    private final String jwtSecret;

    public JwtSecretProvider(@Value("${jwt.secret:}") String jwtSecretEnv) {
        if (jwtSecretEnv == null || jwtSecretEnv.isBlank()) {
            // Gera uma nova chave se não estiver configurada em variáveis de ambiente
            this.jwtSecret = Base64.getEncoder().encodeToString(Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512).getEncoded());
        } else {
            // Usa a chave definida como variável de ambiente
            this.jwtSecret = jwtSecretEnv;
        }
    }

    public String getJwtSecret() {
        return jwtSecret;
    }
}
