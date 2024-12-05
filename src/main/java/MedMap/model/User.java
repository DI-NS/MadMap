package MedMap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Representa uma UBS (Unidade Básica de Saúde) no sistema MedMap.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nome da UBS.
     */
    @Column(name = "nome_ubs", nullable = false)
    @NotBlank(message = "O nome da UBS é obrigatório")
    private String nomeUbs;

    /**
     * Código CNES da UBS em texto puro.
     */
    @Column(name = "cnes", nullable = false, unique = true)
    @NotBlank(message = "O CNES é obrigatório")
    private String cnes;

    /**
     * CNES criptografado para autenticação.
     */
    @JsonIgnore
    @Column(name = "hashed_cnes", nullable = false)
    private String hashedCnes;

    /**
     * Endereço da UBS.
     */
    @Column(nullable = false)
    @NotBlank(message = "O endereço é obrigatório")
    private String address;

    // Construtores, getters e setters
    public User() {
    }

    public User(String nomeUbs, String cnes, String address) {
        this.nomeUbs = nomeUbs;
        this.cnes = cnes;
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public String getNomeUbs() {
        return nomeUbs;
    }

    public void setNomeUbs(String nomeUbs) {
        this.nomeUbs = nomeUbs;
    }

    public String getCnes() {
        return cnes;
    }

    public void setCnes(String cnes) {
        this.cnes = cnes;
    }

    public String getHashedCnes() {
        return hashedCnes;
    }

    public void setHashedCnes(String hashedCnes) {
        this.hashedCnes = hashedCnes;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
