package pe.upc.mckchain.controller.util.util_code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.security.dto.JwtResponse;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.serviceimpl.UserDetailsImpl;

import java.util.Optional;

public class Code_UserAccessValidation {

    public static ResponseEntity<?> ValidacionUsuarioExistente(IUsuarioService usuarioService,
                                                               String emailUsuario) {
        Optional<Usuario> usuario_data =
                usuarioService.BuscarUsuario_By_EmailUsuario(emailUsuario);

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            return Code_UserAccessValidation.ValidacionSignupEstadoCuentaUsuario(usuario);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al determinar el " +
                    "estado de este usuario."),
                    HttpStatus.NOT_FOUND);
        }
    }

    public static ResponseEntity<?> ValidacionSignupEstadoCuentaUsuario(Usuario usuario) {

        switch (usuario.getEstadoUsuario()) {
            case "PENDIENTE":
                return new ResponseEntity<>(new MessageResponse("Ya se ha utilizado este correo electrónico para un " +
                        "proceso de registro previo."),
                        HttpStatus.LOCKED);
            case "ACTIVO":
            case "BLOQUEADO":
            case "INACTIVO":
                return new ResponseEntity<>(new MessageResponse("Ya se encuentra en uso este correo electrónico por " +
                        "un usuario registrado en el sistema."),
                        HttpStatus.LOCKED);
            default:
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al determinar el " +
                        "estado de este usuario."),
                        HttpStatus.NOT_FOUND);
        }
    }

    public static ResponseEntity<?> ValidacionSigninEstadoCuentaMinera(Usuario minera, String jwt,
                                                                       UserDetailsImpl userDetails) {
        switch (userDetails.getEstadoUsuario()) {
            case "PENDIENTE":
                return new ResponseEntity<>(new MessageResponse("Tiene un proceso de registro pendiente por " +
                        "culminar."),
                        HttpStatus.LOCKED);
            case "INACTIVO":
                return new ResponseEntity<>(new MessageResponse("No tiene acceso al sistema por tener su " +
                        "cuenta inactiva. Se requiere contactar con el Administrador."),
                        HttpStatus.LOCKED);
            case "BLOQUEADO":
                return new ResponseEntity<>(new MessageResponse("Se ha bloqueado el acceso al sistema por " +
                        "tener un proceso pendiente en su cuenta."),
                        HttpStatus.LOCKED);
            case "ACTIVO":
                return new ResponseEntity<>(new JwtResponse(
                        jwt,
                        userDetails.getIdUsuario(),
                        userDetails.getUsername(),
                        userDetails.getNombreUsuario(),
                        minera.getMckchainMinera().getIdUsuario(),
                        userDetails.getImagenUsuario(),
                        userDetails.getAuthorities()
                ),
                        HttpStatus.OK);
            default:
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al determinar el " +
                        "estado de este usuario."),
                        HttpStatus.NOT_FOUND);
        }
    }

    public static ResponseEntity<?> ValidacionSigninEstadoCuentaComprador(Usuario comprador, String jwt,
                                                                          UserDetailsImpl userDetails) {
        switch (userDetails.getEstadoUsuario()) {
            case "PENDIENTE":
                return new ResponseEntity<>(new MessageResponse("Tiene un proceso de registro pendiente por " +
                        "culminar."),
                        HttpStatus.LOCKED);
            case "INACTIVO":
                return new ResponseEntity<>(new MessageResponse("No tiene acceso al sistema por tener su " +
                        "cuenta inactiva. Se requiere contactar con el Administrador."),
                        HttpStatus.LOCKED);
            case "BLOQUEADO":
                return new ResponseEntity<>(new MessageResponse("Se ha bloqueado el acceso al sistema por " +
                        "tener un proceso pendiente en su cuenta."),
                        HttpStatus.LOCKED);
            case "ACTIVO":
                return new ResponseEntity<>(new JwtResponse(
                        jwt,
                        userDetails.getIdUsuario(),
                        userDetails.getUsername(),
                        userDetails.getNombreUsuario(),
                        comprador.getMckchainComprador().getIdUsuario(),
                        userDetails.getImagenUsuario(),
                        userDetails.getAuthorities()
                ),
                        HttpStatus.OK);
            default:
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al determinar el " +
                        "estado de este usuario."),
                        HttpStatus.NOT_FOUND);
        }
    }

    public static ResponseEntity<?> ValidacionSigninEstadoCuentaUsuario(Usuario usuario, String jwt,
                                                                        UserDetailsImpl userDetails) {
        switch (userDetails.getEstadoUsuario()) {
            case "PENDIENTE":
                return new ResponseEntity<>(new MessageResponse("Tiene un proceso de registro pendiente por " +
                        "culminar."),
                        HttpStatus.LOCKED);
            case "INACTIVO":
                return new ResponseEntity<>(new MessageResponse("No tiene acceso al sistema por tener su " +
                        "cuenta inactiva. Se requiere contactar con el Administrador."),
                        HttpStatus.LOCKED);
            case "BLOQUEADO":
                return new ResponseEntity<>(new MessageResponse("Se ha bloqueado el acceso al sistema por " +
                        "tener un proceso pendiente en su cuenta."),
                        HttpStatus.LOCKED);
            case "ACTIVO":
                return new ResponseEntity<>(new JwtResponse(
                        jwt,
                        userDetails.getIdUsuario(),
                        userDetails.getUsername(),
                        userDetails.getNombreUsuario(),
                        userDetails.getApellidoUsuario(),
                        usuario.getMineraUsuario().getIdUsuario(),
                        userDetails.getImagenUsuario(),
                        userDetails.getAuthorities()
                ),
                        HttpStatus.OK);
            default:
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al determinar el " +
                        "estado de este usuario."),
                        HttpStatus.NOT_FOUND);
        }
    }
}
