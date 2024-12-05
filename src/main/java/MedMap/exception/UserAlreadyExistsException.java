package MedMap.exception;

/**
 * Exceção lançada quando uma UBS já está registrada com o CNES fornecido.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
