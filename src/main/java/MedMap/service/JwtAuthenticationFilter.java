package MedMap.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64;

/**
 * Filtro que intercepta requisições para validar o token JWT.
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtSecretProvider jwtSecretProvider;

    public JwtAuthenticationFilter(JwtSecretProvider jwtSecretProvider) {
        this.jwtSecretProvider = jwtSecretProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            try {
                String jwtSecret = jwtSecretProvider.getJwtSecret();
                byte[] keyBytes = Base64.getDecoder().decode(jwtSecret);
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String cnes = claims.getSubject();

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(cnes, null, null);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token JWT inválido ou expirado");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}
