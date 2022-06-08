package pe.upc.mckchain.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "utilitytoken")
@Getter
@Setter
public class UtilityToken implements Serializable {

    private static final long serialVersionUID = 1L;

    //Atributos
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id_utilitytoken")
    private UUID idUtilityToken;

    @Column(name = "token_utilitytoken")
    private String tokenUtilityToken;

    @Column(name = "razon_utilitytoken", length = 35)
    private String razonUtilityToken;

    @Column(name = "expiracion_utilitytoken")
    private LocalDateTime expiracionUtilityToken;

    @ManyToOne
    @JoinTable(name = "usuario_utilitytokens",
            joinColumns = @JoinColumn(name = "id_utilitytoken", referencedColumnName = "id_utilitytoken"),
            inverseJoinColumns = @JoinColumn(name = "id_usuario", referencedColumnName = "id_usuario"))
    private Usuario usuarioUtilityToken;

    //Constructores
    public UtilityToken() {
    }

    public UtilityToken(String tokenUtilityToken, String razonUtilityToken, LocalDateTime expiracionUtilityToken,
                        Usuario usuarioUtilityToken) {
        this.tokenUtilityToken = tokenUtilityToken;
        this.razonUtilityToken = razonUtilityToken;
        this.expiracionUtilityToken = expiracionUtilityToken;
        this.usuarioUtilityToken = usuarioUtilityToken;
    }
}
