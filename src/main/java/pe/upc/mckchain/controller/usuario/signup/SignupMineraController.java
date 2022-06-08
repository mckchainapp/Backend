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
import pe.upc.mckchain.controller.util.util_code.Code_SignupUserMethods;
import pe.upc.mckchain.controller.util.util_code.Code_UploadImage;
import pe.upc.mckchain.dto.request.usuario.signup.SignupMineraRequest;
import pe.upc.mckchain.dto.request.usuario.signup.SignupMineraRequestRequest;
import pe.upc.mckchain.dto.response.SignupRequestMineraResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.enums.RolNombre;
import pe.upc.mckchain.model.Minera;
import pe.upc.mckchain.model.Rol;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.*;
import pe.upc.mckchain.tools.MailService;
import pe.upc.mckchain.tools.UtilityMckchain;
import pe.upc.mckchain.validations.UsuarioSignupValidation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class SignupMineraController {

    final
    IUsuarioService usuarioService;

    final
    IMineraService mineraService;

    final
    IUtilityTokenService utilityTokenService;

    final
    IRolService rolService;

    final
    IImagenService imagenService;

    final
    MailService mailService;

    final
    TemplateEngine templateEngine;

    final
    PasswordEncoder passwordEncoder;

    @Value("${admin.id}")
    private UUID id_admin;

    @Value("${front.baseurl}")
    private String baseurl;

    public SignupMineraController(IUsuarioService usuarioService, IMineraService mineraService,
                                  IUtilityTokenService utilityTokenService, IRolService rolService,
                                  IImagenService imagenService, MailService mailService, TemplateEngine templateEngine,
                                  PasswordEncoder passwordEncoder) {
        this.usuarioService = usuarioService;
        this.mineraService = mineraService;
        this.utilityTokenService = utilityTokenService;
        this.rolService = rolService;
        this.imagenService = imagenService;
        this.mailService = mailService;
        this.templateEngine = templateEngine;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/signup/minera_request")
    public ResponseEntity<?> SignupMineraRequest(@RequestBody SignupMineraRequestRequest signupMineraRequestRequest) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_IDUsuario(id_admin);

        if (admin_data.isPresent()) {
            Optional<Minera> mineradata_data = mineraService.BuscarMinera_By_CodigoUnicoMineraAndRucMinera(
                    signupMineraRequestRequest.getCodigounicoMinera(),
                    signupMineraRequestRequest.getRucMinera()
            );

            if (mineradata_data.isPresent()) {
                Minera mineradata = mineradata_data.get();

                Usuario minera = new Usuario();
                minera.setEmailUsuario(signupMineraRequestRequest.getEmailMinera());
                minera.setNombreUsuario(mineradata.getNombreMinera());
                minera.setRucUsuario(mineradata.getRucMinera());
                usuarioService.GuardarUsuario(minera);

                mineradata.setEstadosolicitudMinera("GENERADA");

                //Fecha y Hora Actual Solicitud de Minera
                ZoneId zona_actual = ZoneId.of("America/Lima");
                LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();
                mineradata.setFechasolicitudMinera(fechahora_actual);

                mineradata.setMineraData(minera);
                mineraService.GuardarMinera(mineradata);

                return new ResponseEntity<>(new MessageResponse("Su solicitud ha sido enviada correctamente."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("No se encuentra registrado en REINFO."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Administrador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/signup/minera_request/display/generada")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> MostrarSignupMinerasRequestGenerada() {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_IDUsuario(id_admin);

        if (admin_data.isPresent()) {
            Set<SignupRequestMineraResponse> list_signupminerasrequest = new HashSet<>();

            mineraService.BuscarMineras_By_EstadoSolicitudMinera("GENERADA")
                    .forEach(minera -> list_signupminerasrequest.add(new SignupRequestMineraResponse(
                            minera.getIdMinera(),
                            minera.getCodigounicoMinera(),
                            minera.getRucMinera(),
                            minera.getNombreMinera(),
                            minera.getMineraData().getEmailUsuario(),
                            minera.getFechasolicitudMinera(),
                            FormatFechaSolicitudMinera(minera.getFechasolicitudMinera())
                    )));

            return new ResponseEntity<>(list_signupminerasrequest,
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Administrador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/signup/minera_request/display/approved")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> MostrarSignupMinerasRequestApproved() {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_IDUsuario(id_admin);

        if (admin_data.isPresent()) {
            Set<SignupRequestMineraResponse> list_signupminerasrequest = new HashSet<>();

            mineraService.BuscarMineras_By_EstadoSolicitudMinera("APROBADA")
                    .forEach(minera -> list_signupminerasrequest.add(new SignupRequestMineraResponse(
                            minera.getIdMinera(),
                            minera.getCodigounicoMinera(),
                            minera.getRucMinera(),
                            minera.getNombreMinera(),
                            minera.getMineraData().getEmailUsuario(),
                            minera.getFechasolicitudMinera(),
                            FormatFechaSolicitudMinera(minera.getFechasolicitudMinera())
                    )));

            return new ResponseEntity<>(list_signupminerasrequest,
                    HttpStatus.OK);

        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Administrador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    String FormatFechaSolicitudMinera(LocalDateTime fechahora_solicitud) {

        return fechahora_solicitud.format(DateTimeFormatter.ofPattern("dd-MM-yy"));
    }

    @PostMapping("/signup/minera/{id_minera}/request/send/approved")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> SendSignupRequestApproved(@PathVariable("id_minera") UUID id_minera,
                                                       HttpServletRequest request) {

        Optional<Usuario> admin_data = usuarioService.BuscarUsuario_By_IDUsuario(id_admin);

        if (admin_data.isPresent()) {
            Usuario admin = admin_data.get();

            Optional<Minera> mineradata_data = mineraService.BuscarMinera_By_IDMinera(id_minera);

            if (mineradata_data.isPresent()) {
                Minera mineradata = mineradata_data.get();

                Optional<Usuario> minera_data =
                        usuarioService.BuscarUsuarioMinera_By_IDMineraData(mineradata.getIdMinera());

                if (minera_data.isPresent()) {
                    Usuario minera = minera_data.get();

                    Set<UsuarioSignupValidation> list_mineras_to_validate_send_request = new HashSet<>();

                    usuarioService.ValidarSolicitudRegistroMinera(
                                    admin.getIdUsuario(),
                                    minera.getEmailUsuario())
                            .forEach(mineras -> list_mineras_to_validate_send_request.add(new UsuarioSignupValidation(
                                    admin.getIdUsuario(),
                                    minera.getEmailUsuario()
                            )));

                    if (list_mineras_to_validate_send_request.size() < 1) {
                        String token = UUID.randomUUID() + "-" + UUID.randomUUID();

                        minera.setMckchainMinera(admin);
                        minera.setEstadoUsuario("PENDIENTE");
                        usuarioService.GuardarUsuario(minera);

                        mineradata.setEstadosolicitudMinera("APROBADA");
                        mineraService.GuardarMinera(mineradata);

                        String url = UtilityMckchain.GenerarUrl(request) + "/api/minera_register_gateway?token=" + token;

                        mailService.SignupMineraRequestApproved(
                                minera.getEmailUsuario(),
                                url);

                        //Generando Token: Signup Minera
                        String razon = "Signup Minera";
                        LocalDateTime expiracion = LocalDateTime.now().plusHours(72);

                        Code_AssignMethods.AssignUtilityTokenToUser(token, minera, razon, expiracion, utilityTokenService);

                        return new ResponseEntity<>(new MessageResponse("Se envió la notificación a la Bandeja de Entrada " +
                                "del Solicitante correctamente."),
                                HttpStatus.OK);
                    } else {
                        return new ResponseEntity<>(new MessageResponse("Ya se notificó a dicha Minera previamente."),
                                HttpStatus.CONFLICT);
                    }
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la " +
                            "Minera."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de REINFO de " +
                        "dicha Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Usuario Administrador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/signup/minera/{id_minera}/request/send/rejected")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> SendSignupRequestRejected(@PathVariable("id_minera") UUID id_minera) {

        Optional<Minera> mineradata_data = mineraService.BuscarMinera_By_IDMinera(id_minera);

        if (mineradata_data.isPresent()) {
            Minera mineradata = mineradata_data.get();

            if (Objects.equals(mineradata.getEstadoactividadMinera(), "RECHAZADA")) {
                return new ResponseEntity<>(new MessageResponse("Ya se notificó a dicha Minera previamente."),
                        HttpStatus.CONFLICT);
            } else {
                Optional<Usuario> minera_data =
                        usuarioService.BuscarUsuarioMinera_By_IDMineraData(mineradata.getIdMinera());

                if (minera_data.isPresent()) {
                    Usuario minera = minera_data.get();

                    mineradata.setEstadosolicitudMinera("RECHAZADA");
                    mineraService.GuardarMinera(mineradata);

                    Minera current_mineradata = new Minera(
                            mineradata.getCodigounicoMinera(),
                            mineradata.getEstadoactividadMinera(),
                            mineradata.getRucMinera(),
                            mineradata.getNombreMinera(),
                            mineradata.getEstadosolicitudMinera(),
                            mineradata.getFecharegistroMinera(),
                            mineradata.getFechasolicitudMinera(),
                            mineradata.getDistritoMinera()
                    );
                    mineraService.GuardarMinera(current_mineradata);

                    mailService.SignupMineraRequestRejected(
                            minera.getEmailUsuario());

                    usuarioService.EliminarUsuario(minera.getIdUsuario());

                    return new ResponseEntity<>(new MessageResponse("Se envió la notificación a la Bandeja de Entrada " +
                            "del Solicitante correctamente."),
                            HttpStatus.OK);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la " +
                            "Minera."),
                            HttpStatus.NOT_FOUND);
                }
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de REINFO de " +
                    "dicha Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/minera_register_gateway")
    void RedirectSignupMineraRequest(HttpServletResponse response, @Param(value = "token") String token)
            throws IOException {

        Optional<UtilityToken> utilitytoken_data = utilityTokenService.BuscarUtilityToken_By_Token(token);

        if (utilitytoken_data.isPresent()) {
            response.sendRedirect(baseurl + "/signup/minera/" + token);
        } else {
            response.sendRedirect(baseurl + "/error/403");
        }
    }

    @PostMapping("/signup/minera")
    public ResponseEntity<?> SignupMineraProcess(@RequestBody SignupMineraRequest signupMineraRequest) {

        Optional<UtilityToken> utilitytoken_data =
                utilityTokenService.BuscarUtilityToken_By_Token(signupMineraRequest.getUtilitytokenUsuario());

        if (utilitytoken_data.isPresent()) {
            UtilityToken utilitytoken = utilitytoken_data.get();

            Optional<Usuario> minera_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken.getIdUtilityToken());

            if (minera_data.isPresent()) {
                Usuario minera = minera_data.get();

                Optional<Rol> rol_data = rolService.BuscarRol_Nombre(RolNombre.ROLE_MINERA);

                if (rol_data.isPresent()) {
                    Rol rol = rol_data.get();

                    Set<UsuarioSignupValidation> list_mineras_to_validate = new HashSet<>();

                    usuarioService.ValidarProcesoRegistroMinera(
                                    id_admin,
                                    minera.getEmailUsuario(),
                                    signupMineraRequest.getUsernameUsuario())
                            .forEach(usuariominera -> list_mineras_to_validate.add(new UsuarioSignupValidation(
                                    id_admin,
                                    minera.getEmailUsuario(),
                                    signupMineraRequest.getUsernameUsuario()
                            )));

                    if (list_mineras_to_validate.size() < 1) {
                        try {
                            minera.setUsernameUsuario(signupMineraRequest.getUsernameUsuario());
                            Code_AssignMethods.AssignSignupDataToUser(minera, usuarioService, passwordEncoder,
                                    signupMineraRequest.getPasswordUsuario());

                            //Asignando Foto por Defecto: Minera
                            InputStream fotoStream = getClass().getResourceAsStream("/static/img/MineraUser.png");
                            Code_UploadImage.AssignImage(minera, "/logos/", fotoStream, imagenService);

                            //Asignando Rol: Minera
                            Code_AssignMethods.AssignRolToUser(minera, rol, rolService, usuarioService);

                            utilityTokenService.EliminarUtilityToken(utilitytoken.getIdUtilityToken());

                            return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente."),
                                    HttpStatus.ACCEPTED);
                        } catch (Exception e) {
                            Code_SignupUserMethods.RollbackSignup_Minera(minera, rol, utilitytoken, usuarioService,
                                    rolService, utilityTokenService);

                            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al asignar la foto de " +
                                    "perfil por defecto." + e),
                                    HttpStatus.EXPECTATION_FAILED);
                        }
                    } else {
                        return new ResponseEntity<>(new MessageResponse("Ya existe una Minera con dichos datos."),
                                HttpStatus.CONFLICT);
                    }
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al otorgar sus permisos " +
                            "correspondientes."),
                            HttpStatus.NOT_FOUND);
                }
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
