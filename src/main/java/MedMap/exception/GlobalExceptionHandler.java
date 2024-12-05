//package MedMap.exception;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.bind.annotation.RestControllerAdvice;
//
//@RestControllerAdvice
//public class GlobalExceptionHandler {
//
//    @ExceptionHandler(UserAlreadyExistsException.class)
//    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e, HttpServletRequest request) {
//        if (isSwaggerRequest(request)) {
//            return ResponseEntity.ok("Swagger exception ignored");
//        }
//        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
//    }
//
//    @ExceptionHandler(InvalidCredentialsException.class)
//    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e, HttpServletRequest request) {
//        if (isSwaggerRequest(request)) {
//            return ResponseEntity.ok("Swagger exception ignored");
//        }
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
//    }
//
//    private boolean isSwaggerRequest(HttpServletRequest request) {
//        String requestURI = request.getRequestURI();
//        return requestURI.startsWith("/v3/api-docs") || requestURI.startsWith("/swagger-ui") || requestURI.startsWith("/swagger-ui.html");
//    }
//}
