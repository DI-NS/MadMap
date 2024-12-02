package MedMap.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome_ubs", nullable = false) // Banco exige valor
    @NotBlank(message = "O nome da UBS é obrigatório") // Validação no backend
    private String nomeUbs;

    @Column(nullable = false) // Banco exige valor
    @NotBlank(message = "O CNES é obrigatório") // Validação no backend
    private String cnes;

    @Column(nullable = false) // Banco exige valor
    @NotBlank(message = "O endereço é obrigatório") // Validação no backend
    private String address;

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}