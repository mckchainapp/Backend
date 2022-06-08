package pe.upc.mckchain.controller.util.util_code;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.model.Rol;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.IImagenService;
import pe.upc.mckchain.service.IRolService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IUtilityTokenService;
import pe.upc.mckchain.validations.UsuarioSignupValidation;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

public class Code_AssignMethods {

    public static void AssignRolToUser(Usuario usuario, Rol rol, IRolService rolService, IUsuarioService usuarioService) {

        Set<Rol> roles = new HashSet<>();
        roles.add(rol);

        rolService.GuardarRol(rol);

        usuario.setRolesUsuario(roles);

        usuarioService.GuardarUsuario(usuario);
    }

    public static void AssignUtilityTokenToUser(String token, Usuario usuario, String razon, LocalDateTime expiracion,
                                                IUtilityTokenService utilityTokenService) {

        UtilityToken utilityToken = new UtilityToken(token, razon, expiracion, usuario);

        utilityTokenService.GuardarUtilityToken(utilityToken);
    }

    public static void AssignSignupDataToUser(Usuario usuario, IUsuarioService usuarioService,
                                              PasswordEncoder passwordEncoder, String passwordUsuario) {

        usuario.setPasswordUsuario(passwordEncoder.encode(passwordUsuario));

        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();
        LocalDate fecha_actual = fechahora_actual.toLocalDate();
        usuario.setFecharegistroUsuario(fecha_actual);

        //Cambiando Estado de Cuenta a ACTIVO
        usuario.setEstadoUsuario("ACTIVO");

        usuarioService.GuardarUsuario(usuario);
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    public static ResponseEntity<?> AssignSignupDataToMineraUser(IUsuarioService usuarioService,
                                                                 IImagenService imagenService, IRolService rolService,
                                                                 IUtilityTokenService utilityTokenService,
                                                                 PasswordEncoder passwordEncoder, Optional<Rol> rol_data,
                                                                 Usuario usuario, UtilityToken utilitytoken,
                                                                 String usernameUsuario, String nombreUsuario,
                                                                 String apellidoUsuario, String passwordUsuario) {

        if (rol_data.isPresent()) {
            Rol rol = rol_data.get();

            Set<UsuarioSignupValidation> list_operarios_to_validate = new HashSet<>();

            usuarioService.ValidarProcesoRegistroUsuario(
                            usuario.getMineraUsuario().getIdUsuario(),
                            usuario.getEmailUsuario(),
                            usernameUsuario)
                    .forEach(usuarios -> list_operarios_to_validate.add(new UsuarioSignupValidation(
                            usuario.getMineraUsuario().getIdUsuario(),
                            usuario.getEmailUsuario(),
                            usernameUsuario
                    )));

            if (list_operarios_to_validate.size() < 1) {
                try {
                    usuario.setUsernameUsuario(usernameUsuario);
                    usuario.setNombreUsuario(nombreUsuario);
                    usuario.setApellidoUsuario(apellidoUsuario);
                    Code_AssignMethods.AssignSignupDataToUser(usuario, usuarioService, passwordEncoder,
                            passwordUsuario);

                    //Asignando Foto por Defecto: Usuario Simple
                    InputStream fotoStream = Code_AssignMethods.class.getResourceAsStream("/static/img/SingleUser.png");
                    Code_UploadImage.AssignImage(usuario, "/photos/", fotoStream, imagenService);

                    //Asignando Rol: Operario
                    Code_AssignMethods.AssignRolToUser(usuario, rol, rolService, usuarioService);

                    utilityTokenService.EliminarUtilityToken(utilitytoken.getIdUtilityToken());

                    return new ResponseEntity<>(new MessageResponse("Se ha registrado satisfactoriamente."),
                            HttpStatus.ACCEPTED);
                } catch (Exception e) {
                    Code_SignupUserMethods.RollbackSignup_Usuario(usuario, rol, utilitytoken, usuarioService,
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
    }

    public static ResponseEntity<?> AssignFotoPerfil(@RequestPart("foto") MultipartFile foto, Imagen foto_perfil,
                                                     IImagenService imagenService) {
        try {
            if (!foto.isEmpty()) {
                //Obteniendo Extension del Archivo Seleccionado
                String separador_foto = Pattern.quote(".");
                String[] formato_foto = Objects.requireNonNull(foto.getOriginalFilename()).split(separador_foto);

                String nuevonombre_foto = Code_UploadImage.UpdateImageName(foto_perfil.getNombreImagen())
                        + "." + formato_foto[formato_foto.length - 1];

                String url_foto = Code_UploadImage.SendFileUrl("/photos/", nuevonombre_foto);

                foto_perfil.setNombreImagen(nuevonombre_foto);
                foto_perfil.setUrlImagen(url_foto);
                foto_perfil.setArchivoImagen(foto.getBytes());
                foto_perfil.setTipoarchivoImagen(foto.getContentType());

                imagenService.GuardarImagen(foto_perfil);

                return new ResponseEntity<>(new MessageResponse("Se ha actualizado su foto de perfil " +
                        "satisfactoriamente."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("No se ha seleccionado un archivo."),
                        HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e),
                    HttpStatus.EXPECTATION_FAILED);
        }
    }
}
