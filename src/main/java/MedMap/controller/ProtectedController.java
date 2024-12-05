package MedMap.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador de exemplo para um endpoint protegido.
 */
@RestController
public class ProtectedController {

    /**
     * Endpoint protegido que requer autenticação via JWT.
     * Se o token for válido, retornará 200 OK.
     */
    @GetMapping("/some/protected/endpoint")
    public ResponseEntity<String> protectedEndpoint() {
        return ResponseEntity.ok("OK");
    }
}
