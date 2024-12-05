package MedMap.service;

import MedMap.exception.InvalidCredentialsException;
import MedMap.exception.UserAlreadyExistsException;
import MedMap.model.User;
import MedMap.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela lógica de autenticação e registro.
 */
@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, TokenService tokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    /**
     * Registra uma nova UBS no sistema.
     *
     * @param user Dados da UBS a ser registrada.
     * @return Mensagem de sucesso.
     */
    public String register(User user) {
        // Verifica se já existe uma UBS com o mesmo CNES
        if (userRepository.findByCnes(user.getCnes()).isPresent()) {
            throw new UserAlreadyExistsException("Uma UBS com esse CNES já está registrada.");
        }

        // Gera o hash do CNES internamente
        user.setHashedCnes(passwordEncoder.encode(user.getCnes()));

        userRepository.save(user);
        return "UBS registrada com sucesso!";
    }

    /**
     * Autentica uma UBS com base no CNES.
     *
     * @param cnes Código CNES da UBS.
     * @return Token JWT para acesso às rotas protegidas.
     */
    public String login(String nomeUbs, String cnes) {
        User user = userRepository.findByNomeUbs(nomeUbs)
                .orElseThrow(() -> new InvalidCredentialsException("UBS não encontrada ou dados inválidos"));

        if (!passwordEncoder.matches(cnes, user.getHashedCnes())) {
            throw new InvalidCredentialsException("CNES inválido");
        }

        return tokenService.generateToken(user);
    }
}
