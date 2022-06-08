package pe.upc.mckchain.controller.usuario;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.controller.util.util_code.Code_AssignMethods;
import pe.upc.mckchain.dto.request.usuario.general.EmailRequest;
import pe.upc.mckchain.dto.request.usuario.general.UpdatePasswordRequest;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IUtilityTokenService;
import pe.upc.mckchain.tools.MailService;
import pe.upc.mckchain.tools.UtilityMckchain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class RestorePasswordController {

    final
    IUsuarioService usuarioService;

    final
    IUtilityTokenService utilityTokenService;

    final
    MailService mailService;

    final
    PasswordEncoder passwordEncoder;

    @Value("${front.baseurl}")
    private String baseurl;

    public RestorePasswordController(IUsuarioService usuarioService, IUtilityTokenService utilityTokenService,
                                     MailService mailService, PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.utilityTokenService = utilityTokenService;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/restore_password/request")
    public ResponseEntity<?> RestorePasswordRequest(@RequestBody EmailRequest emailRequest,
                                                    HttpServletRequest request) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_EmailUsuario(emailRequest.getEmailUsuario());

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            if (Objects.equals(usuario.getEstadoUsuario(), "PENDIENTE")) {
                return new ResponseEntity<>(new MessageResponse("No puede iniciar este proceso si aún no culminó un " +
                        "proceso de registro previamente."),
                        HttpStatus.LOCKED);
            } else if (Objects.equals(usuario.getEstadoUsuario(), "INACTIVO")) {
                return new ResponseEntity<>(new MessageResponse("Su cuenta requiere ser gestionada por el Administrador " +
                        "por presentar un estado de Inactividad."),
                        HttpStatus.LOCKED);
            } else if (Objects.equals(usuario.getEstadoUsuario(), "ACTIVO")) {
                Set<UtilityToken> lista_utilitytoken =
                        new HashSet<>(utilityTokenService.BuscarUtilityToken_By_IDUsuarioAndRazonUtilityToken(
                                usuario.getIdUsuario(),
                                "Restore Password"));

                if (lista_utilitytoken.size() < 1) {
                    String token = UUID.randomUUID() + "-" + UUID.randomUUID();

                    String url = UtilityMckchain.GenerarUrl(request) + "/api/restore_password_gateway?token=" + token;

                    mailService.RestorePassword(
                            emailRequest.getEmailUsuario(),
                            url);

                    //Generando Token: Restore Password
                    String razon = "Restore Password";

                    //Fecha y Hora Actual
                    ZoneId zona_actual = ZoneId.of("America/Lima");
                    LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();
                    LocalDateTime expiracion = fechahora_actual.plusMinutes(10);

                    Code_AssignMethods.AssignUtilityTokenToUser(token, usuario, razon, expiracion, utilityTokenService);

                    return new ResponseEntity<>(new MessageResponse("Revise su bandeja de entrada para continuar con el " +
                            "proceso de Restauración de Contraseña. Recuerde que dispone de no más de 10 minutos para " +
                            "culminar con el proceso. De lo contrario, deberá efectuar una nueva solicitud."),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ya se solicitó un proceso de Restablecimiento " +
                            "previamente con este correo electrónico."),
                            HttpStatus.LOCKED);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("No se ha determinado el estado actual de su usuario."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("No se encuentra el email solicitado en nuestro sistema."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/restore_password_gateway")
    void RedirectSignupAdminRequest(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            Optional<Usuario> usuario_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken_data.get().getIdUtilityToken());

            if (usuario_data.isPresent()) {
                Usuario usuario = usuario_data.get();

                usuario.setEstadoUsuario("BLOQUEADO");

                usuarioService.GuardarUsuario(usuario);

                response.sendRedirect(baseurl + "/restore/password/" + token);
            } else {
                response.sendRedirect(baseurl + "/error/403");
            }
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PutMapping("/restore_password/update")
    public ResponseEntity<?> UpdatePasswordProcess(@RequestBody UpdatePasswordRequest updatePasswordRequest) {

        Optional<UtilityToken> utilitytoken_data =
                utilityTokenService.BuscarUtilityToken_By_Token(updatePasswordRequest.getUtilitytokenUsuario());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> usuario_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken.getIdUtilityToken());

            if (usuario_data.isPresent()) {
                Usuario usuario = usuario_data.get();

                usuario.setPasswordUsuario(passwordEncoder.encode(updatePasswordRequest.getPasswordUsuario()));

                //Cambiando Estado de Cuenta: ACTIVO
                usuario.setEstadoUsuario("ACTIVO");

                usuarioService.GuardarUsuario(usuario);

                utilityTokenService.EliminarUtilityToken(utilitytoken.getIdUtilityToken());

                return new ResponseEntity<>(new MessageResponse("Se ha actualizado su contraseña satisfactoriamente."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al procesar su solicitud."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("El proceso de Restablecimiento de Contraseña ya no se " +
                    "encuentra disponible."),
                    HttpStatus.GONE);
        }
    }
}
