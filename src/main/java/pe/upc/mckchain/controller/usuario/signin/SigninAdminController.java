package pe.upc.mckchain.controller.usuario.signin;


import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.upc.mckchain.dto.request.usuario.SigninRequest;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.security.dto.JwtResponse;
import pe.upc.mckchain.security.jwt.JwtProvider;
import pe.upc.mckchain.service.IImagenService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.serviceimpl.UserDetailsImpl;

import java.io.InputStream;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SigninAdminController {

    final
    AuthenticationManager authenticationManager;

    final
    JwtProvider jwtProvider;

    final
    IUsuarioService usuarioService;

    final
    IImagenService imagenService;

    public SigninAdminController(AuthenticationManager authenticationManager, JwtProvider jwtProvider,
                                 IUsuarioService usuarioService, IImagenService imagenService) {
        this.authenticationManager = authenticationManager;
        this.jwtProvider = jwtProvider;
        this.usuarioService = usuarioService;
        this.imagenService = imagenService;
    }

    @PostMapping("/signin/admin")
    public ResponseEntity<?> SigninAdmin(@RequestBody SigninRequest signinRequest) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_UsernameOrEmail(signinRequest.getUsernameUsuario());

        if (admin_data.isPresent()) {
            Usuario admin = admin_data.get();

            Imagen logo = admin.getImagenUsuario();

            if (logo == null) {
                //Asignando Default Logo: Admin
                try {
                    String nombre_logo = UUID.randomUUID() + "-" + UUID.randomUUID()
                            + ".png";

                    String url_logo = ServletUriComponentsBuilder
                            .fromCurrentContextPath()
                            .path("/logos/")
                            .path(nombre_logo)
                            .toUriString();

                    InputStream logoStream = getClass().getResourceAsStream("/static/img/AdminUser.png");
                    assert logoStream != null;
                    byte[] file_logo = IOUtils.toByteArray(logoStream);

                    Imagen imagen = new Imagen(
                            nombre_logo,
                            "image/png",
                            url_logo,
                            file_logo,
                            admin
                    );

                    imagenService.GuardarImagen(imagen);
                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al iniciar sesión." + e),
                            HttpStatus.EXPECTATION_FAILED);
                }
            }

            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    admin.getUsernameUsuario(),
                    signinRequest.getPasswordUsuario())
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtProvider.generateJwtToken(authentication);

            UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

            if (userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                if (userDetails.getEstadoUsuario().equals("ACTIVO")) {
                    return new ResponseEntity<>(new JwtResponse(
                            jwt,
                            userDetails.getIdUsuario(),
                            userDetails.getUsername(),
                            userDetails.getNombreUsuario(),
                            userDetails.getApellidoUsuario(),
                            userDetails.getImagenUsuario(),
                            userDetails.getAuthorities()
                    ),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("El Usuario no se encuentra habilitado para acceder " +
                            "al sistema."),
                            HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("No cumple con los permisos para acceder al sistema."),
                        HttpStatus.LOCKED);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Usuario no encontrado en el sistema."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
