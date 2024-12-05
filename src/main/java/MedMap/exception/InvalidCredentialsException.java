package MedMap.exception;

/**
 * Exceção lançada quando as credenciais fornecidas são inválidas.
 */
public class InvalidCredentialsException extends RuntimeException {
    public InvalidCredentialsException(String message) {
        super(message);
    }
}
