package pe.upc.mckchain.controller.usuario.signin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.controller.util.util_code.Code_UserAccessValidation;
import pe.upc.mckchain.dto.request.usuario.SigninRequest;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.security.jwt.JwtProvider;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.serviceimpl.UserDetailsImpl;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SigninUsuariosMineraController {

    final
    AuthenticationManager authenticationManager;

    final
    JwtProvider jwtProvider;

    final
    IUsuarioService usuarioService;

    public SigninUsuariosMineraController(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                                          IUsuarioService usuarioService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/signin/operario")
    public ResponseEntity<?> SigninOperario(@RequestBody SigninRequest signinRequest) {

        Optional<Usuario> operario_data =
                usuarioService.BuscarUsuario_By_UsernameOrEmail(signinRequest.getUsernameUsuario());

        if (operario_data.isPresent()) {
            Usuario operario = operario_data.get();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(operario.getUsernameUsuario(),
                            signinRequest.getPasswordUsuario())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_OPERARIO"))) {

                return Code_UserAccessValidation.ValidacionSigninEstadoCuentaUsuario(operario, jwt, userDetails);
            } else {
                return new ResponseEntity<>(new MessageResponse("No cumple con los permisos para acceder al sistema."),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Usuario no encontrado en el sistema."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signin/encargadolaboratorio")
    public ResponseEntity<?> SigninEncargadoLaboratorio(@RequestBody SigninRequest signinRequest) {

        Optional<Usuario> encargadolaboratorio_data =
                usuarioService.BuscarUsuario_By_UsernameOrEmail(signinRequest.getUsernameUsuario());

        if (encargadolaboratorio_data.isPresent()) {
            Usuario encargadolaboratorio = encargadolaboratorio_data.get();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(encargadolaboratorio.getUsernameUsuario(),
                            signinRequest.getPasswordUsuario())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ENCARGADOLABORATORIO"))) {

                return Code_UserAccessValidation.ValidacionSigninEstadoCuentaUsuario(encargadolaboratorio, jwt, userDetails);
            } else {
                return new ResponseEntity<>(new MessageResponse("No cumple con los permisos para acceder al sistema."),
                        HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Usuario no encontrado en el sistema."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
