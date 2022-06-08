package pe.upc.mckchain.security.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import pe.upc.mckchain.dto.response.general.ImagenResponse;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
public class JwtResponse {

    //Atributos
    private String type = "Bearer";
    private String token;
    private UUID idUsuario;
    private String usernameUsuario;
    private String nombreUsuario;
    private ImagenResponse imagenUsuario;

    @JsonInclude(Include.NON_NULL)
    private String apellidoUsuario;

    @JsonInclude(Include.NON_NULL)
    private UUID idAdmin;

    @JsonInclude(Include.NON_NULL)
    private UUID idMinera;

    private Collection<? extends GrantedAuthority> authorities;

    //Constructores
    public JwtResponse() {
    }

    //JWT Exclusivo de Administrador
    public JwtResponse(String token, UUID idUsuario, String usernameUsuario, String nombreUsuario, String apellidoUsuario,
                       ImagenResponse imagenUsuario, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.usernameUsuario = usernameUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.imagenUsuario = imagenUsuario;
        this.authorities = authorities;
    }

    //JWT Exclusivo de Minera
    public JwtResponse(String token, UUID idUsuario, String usernameUsuario, String nombreUsuario, UUID idAdmin,
                       ImagenResponse imagenUsuario, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.usernameUsuario = usernameUsuario;
        this.nombreUsuario = nombreUsuario;
        this.idAdmin = idAdmin;
        this.imagenUsuario = imagenUsuario;
        this.authorities = authorities;
    }

    public JwtResponse(String token, UUID idUsuario, String usernameUsuario, String nombreUsuario,
                       String apellidoUsuario, UUID idMinera, ImagenResponse imagenUsuario,
                       Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.usernameUsuario = usernameUsuario;
        this.nombreUsuario = nombreUsuario;
        this.apellidoUsuario = apellidoUsuario;
        this.idMinera = idMinera;
        this.imagenUsuario = imagenUsuario;
        this.authorities = authorities;
    }
}
