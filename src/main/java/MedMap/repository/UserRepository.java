package MedMap.repository;

import MedMap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByNomeUbsAndCnes(String nomeUbs, String cnes); // Atualizado para refletir a propriedade 'nome_ubs'
}