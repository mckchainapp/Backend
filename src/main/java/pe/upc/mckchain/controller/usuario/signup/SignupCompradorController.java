package pe.upc.mckchain.controller.usuario.signup;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.controller.util.util_code.Code_AssignMethods;
import pe.upc.mckchain.controller.util.util_code.Code_UploadImage;
import pe.upc.mckchain.controller.util.util_code.Code_UserAccessValidation;
import pe.upc.mckchain.dto.request.usuario.general.UtilityTokenRequest;
import pe.upc.mckchain.dto.request.usuario.signup.SignupCompradorRequest;
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
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupCompradorController {

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

    @Value("${front.baseurl}")
    private String baseurl;

    @Value("${admin.id}")
    private UUID id_admin;

    public SignupCompradorController(PasswordEncoder passwordEncoder, IUsuarioService usuarioService,
                                     IRolService rolService, IImagenService imagenService,
                                     IUtilityTokenService utilityTokenService, MailService mailService) {
        this.passwordEncoder = passwordEncoder;
        this.usuarioService = usuarioService;
        this.rolService = rolService;
        this.imagenService = imagenService;
        this.utilityTokenService = utilityTokenService;
        this.mailService = mailService;
    }

    @PostMapping("/signup/comprador")
    public ResponseEntity<?> SignupComprador(@RequestBody SignupCompradorRequest signupCompradorRequest,
                                             HttpServletRequest request) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_IDUsuario(id_admin);

        if (admin_data.isPresent()) {
            Usuario admin = admin_data.get();

            Set<UsuarioSignupValidation> list_compradores_to_validate = new HashSet<>();

            usuarioService.ValidarRegistroComprador(
                            id_admin,
                            signupCompradorRequest.getRucUsuario())
                    .forEach(operario -> list_compradores_to_validate.add(new UsuarioSignupValidation(
                            id_admin,
                            signupCompradorRequest.getRucUsuario()
                    )));

            if (list_compradores_to_validate.size() < 1) {
                String token = UUID.randomUUID() + "-" + UUID.randomUUID();

                Usuario comprador = new Usuario(
                        signupCompradorRequest.getNombreUsuario(),
                        signupCompradorRequest.getApellidoUsuario(),
                        signupCompradorRequest.getRucUsuario(),
                        signupCompradorRequest.getEmailUsuario(),
                        signupCompradorRequest.getUsernameUsuario(),
                        passwordEncoder.encode(signupCompradorRequest.getPasswordUsuario()),
                        "PENDIENTE",
                        admin);

                usuarioService.GuardarUsuario(comprador);

                String url = UtilityMckchain.GenerarUrl(request) + "/api/comprador_register_gateway?token=" + token;

                mailService.CompradorVerifyEmail(
                        signupCompradorRequest.getEmailUsuario(),
                        url);

                //Generando Token: Signup Operario
                String razon = "Signup Comprador";
                LocalDateTime expiracion = LocalDateTime.now().plusHours(72);

                Code_AssignMethods.AssignUtilityTokenToUser(token, comprador, razon, expiracion, utilityTokenService);

                return new ResponseEntity<>(new MessageResponse("Se envió un Correo a su Bandeja de Entrada para " +
                        "culminar con su Registro."),
                        HttpStatus.OK);
            } else {
                return Code_UserAccessValidation.ValidacionUsuarioExistente(usuarioService,
                        signupCompradorRequest.getEmailUsuario());
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Administrador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comprador_register_gateway")
    void RedirectSignupComprador(HttpServletResponse response, @Param(value = "token") String token) throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/comprador/" + token);
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PostMapping("/signup/comprador/verify")
    public ResponseEntity<?> CompradorVerifyAccount(@RequestBody UtilityTokenRequest utilityTokenRequest) {

        Optional<UtilityToken> utilitytoken_data =
                utilityTokenService.BuscarUtilityToken_By_Token(utilityTokenRequest.getUtilityToken());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> comprador_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken.getIdUtilityToken());

            if (comprador_data.isPresent()) {
                Usuario comprador = comprador_data.get();

                Optional<Rol> rol_data = rolService.BuscarRol_Nombre(RolNombre.ROLE_COMPRADOR);

                if (rol_data.isPresent()) {
                    Rol rol = rol_data.get();

                    try {
                        comprador.setEstadoUsuario("ACTIVO");

                        //---Asignando Fecha de Registro Actual
                        ZoneId zona_actual = ZoneId.of("America/Lima");
                        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();
                        LocalDate fecha_actual = fechahora_actual.toLocalDate();
                        comprador.setFecharegistroUsuario(fecha_actual);

                        usuarioService.GuardarUsuario(comprador);

                        //Asignando Rol: Agricultor
                        Code_AssignMethods.AssignRolToUser(comprador, rol, rolService, usuarioService);

                        //Asignando Foto por Defecto: Comprador
                        InputStream fotoStream = getClass().getResourceAsStream("/static/img/ClientUser.png");
                        Code_UploadImage.AssignImage(comprador, "/photos/", fotoStream, imagenService);

                        utilityTokenService.EliminarUtilityToken(utilitytoken.getIdUtilityToken());

                        return new ResponseEntity<>(new MessageResponse("Se terminó de verificar correctamente su " +
                                "cuenta."),
                                HttpStatus.ACCEPTED);
                    } catch (Exception e) {
                        //--Rollback Rol
                        if (comprador.getRolesUsuario() != null) {
                            Set<Usuario> list_usuarios = rol.getUsuariosRoles();
                            list_usuarios.remove(comprador);

                            rolService.GuardarRol(rol);
                        }

                        //--Rollback UtilityToken
                        if (comprador.getUtilitytokensUsuario() != null) {
                            utilityTokenService.GuardarUtilityToken(utilitytoken);
                        }

                        return new ResponseEntity<>(new MessageResponse("Ocurrió un error al asignar la foto de " +
                                "perfil por defecto." + e),
                                HttpStatus.EXPECTATION_FAILED);
                    }
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Rol Definido."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Comprador."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error en el proceso de verificación."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
