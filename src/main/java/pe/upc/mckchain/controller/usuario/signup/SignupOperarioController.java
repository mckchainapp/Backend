package pe.upc.mckchain.controller.usuario.signup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.TemplateEngine;
import pe.upc.mckchain.controller.util.util_code.Code_AssignMethods;
import pe.upc.mckchain.controller.util.util_code.Code_UserAccessValidation;
import pe.upc.mckchain.dto.request.usuario.general.EmailRequest;
import pe.upc.mckchain.dto.request.usuario.signup.SignupUsuarioRequest;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.enums.RolNombre;
import pe.upc.mckchain.model.Rol;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.IImagenService;
import pe.upc.mckchain.service.IRolService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IUtilityTokenService;
import pe.upc.mckchain.tools.MailService;
import pe.upc.mckchain.tools.UtilityMckchain;
import pe.upc.mckchain.validations.UsuarioSignupValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupOperarioController {

    final
    PasswordEncoder passwordEncoder;

    final
    IUsuarioService usuarioService;

    final
    IRolService rolService;

    final
    IImagenService imagenService;

    final
    IUtilityTokenService utilityTokenService;

    final
    MailService mailService;

    final
    TemplateEngine templateEngine;

    @Value("${front.baseurl}")
    private String baseurl;

    public SignupOperarioController(PasswordEncoder passwordEncoder, IUsuarioService usuarioService,
                                    IRolService rolService, IImagenService imagenService,
                                    IUtilityTokenService utilityTokenService, MailService mailService,
                                    TemplateEngine templateEngine) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.imagenService = imagenService;
        this.utilityTokenService = utilityTokenService;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
    }

    @PostMapping("/minera/{id_minera}/signup/operario_request")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> SignupOperarioRequest(@PathVariable("id_minera") UUID id_minera,
                                                   @RequestBody EmailRequest emailRequest,
                                                   HttpServletRequest request) {

        Set<UsuarioSignupValidation> list_operarios_to_validate = new HashSet<>();

        usuarioService.ValidarSolicitudRegistroUsuario(
                        id_minera,
                        emailRequest.getEmailUsuario())
                .forEach(operario -> list_operarios_to_validate.add(new UsuarioSignupValidation(
                        id_minera,
                        emailRequest.getEmailUsuario()
                )));

        if (list_operarios_to_validate.size() < 1) {
            String token = UUID.randomUUID() + "-" + UUID.randomUUID();

            Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

            if (minera_data.isPresent()) {
                Usuario minera = minera_data.get();

                Usuario operario = new Usuario(
                        emailRequest.getEmailUsuario(),
                        "PENDIENTE"
                );

                usuarioService.GuardarUsuario(operario);

                //Asignando Usuario a Minera
                operario.setMineraUsuario(minera);
                usuarioService.GuardarUsuario(operario);

                String url = UtilityMckchain.GenerarUrl(request) + "/api/operario_register_gateway?token=" + token;

                mailService.UserRequestEmail(
                        emailRequest.getEmailUsuario(),
                        url);

                //Generando Token: Signup Operario
                String razon = "Signup Operario";
                LocalDateTime expiracion = LocalDateTime.now().plusHours(72);

                Code_AssignMethods.AssignUtilityTokenToUser(token, operario, razon, expiracion, utilityTokenService);

                return new ResponseEntity<>(new MessageResponse("Se envió el Correo a la Bandeja de Entrada del " +
                        "Solicitante correctamente."),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al asignar el Usuario Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return Code_UserAccessValidation.ValidacionUsuarioExistente(usuarioService, emailRequest.getEmailUsuario());
        }
    }

    @GetMapping("/operario_register_gateway")
    void RedirectSignupOperario(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/operario/" + token);
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PostMapping("/signup/operario")
    public ResponseEntity<?> SignupOperarioProcess(@RequestBody SignupUsuarioRequest signupUsuarioRequest) {

        Optional<UtilityToken> utilitytoken_data =
                utilityTokenService.BuscarUtilityToken_By_Token(signupUsuarioRequest.getUtilitytokenUsuario());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> operario_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken.getIdUtilityToken());

            if (operario_data.isPresent()) {
                Usuario operario = operario_data.get();

                Optional<Rol> rol_data = rolService.BuscarRol_Nombre(RolNombre.ROLE_OPERARIO);

                return Code_AssignMethods.AssignSignupDataToMineraUser(usuarioService, imagenService, rolService,
                        utilityTokenService, passwordEncoder, rol_data, operario, utilitytoken,
                        signupUsuarioRequest.getUsernameUsuario(), signupUsuarioRequest.getNombreUsuario(),
                        signupUsuarioRequest.getApellidoUsuario(), signupUsuarioRequest.getPasswordUsuario());
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error durante el proceso de Registro."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("El proceso de registro ya no se encuentra disponible."),
                    HttpStatus.GONE);
        }
    }
}