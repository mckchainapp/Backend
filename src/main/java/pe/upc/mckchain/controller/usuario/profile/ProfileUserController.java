package pe.upc.mckchain.controller.usuario.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.upc.mckchain.controller.util.util_code.Code_AssignMethods;
import pe.upc.mckchain.dto.request.usuario.profile.UpdateUsuarioInfoRequest;
import pe.upc.mckchain.dto.response.general.ImagenResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.dto.response.profile.UsuarioProfileResponse;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.service.IImagenService;
import pe.upc.mckchain.service.IUsuarioService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProfileUserController {

    final
    IUsuarioService usuarioService;

    final
    IImagenService imagenService;

    public ProfileUserController(IUsuarioService usuarioService, IImagenService imagenService) {
        this.usuarioService = usuarioService;
        this.imagenService = imagenService;
    }

    @GetMapping("/usuario/{id_usuario}/profile")
    @PreAuthorize("hasRole('ROLE_OPERARIO') or hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> UserProfile(@PathVariable("id_usuario") UUID id_usuario) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_IDUsuario(id_usuario);

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            Optional<Usuario> minera_data =
                    usuarioService.BuscarUsuario_By_IDUsuario(usuario.getMineraUsuario().getIdUsuario());

            if (minera_data.isPresent()) {
                Usuario minera = minera_data.get();

                return new ResponseEntity<>(new UsuarioProfileResponse(
                        usuario.getIdUsuario(),
                        usuario.getNombreUsuario(),
                        usuario.getApellidoUsuario(),
                        usuario.getTelefonoUsuario(),
                        usuario.getEmailUsuario(),
                        new ImagenResponse(
                                usuario.getImagenUsuario().getNombreImagen(),
                                usuario.getImagenUsuario().getUrlImagen()
                        ),
                        minera.getNombreUsuario(),
                        new ImagenResponse(
                                minera.getImagenUsuario().getNombreImagen(),
                                minera.getImagenUsuario().getUrlImagen()
                        )
                ),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la " +
                        "Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Usuario."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/usuario/{id_usuario}/profile/update/info")
    @PreAuthorize("hasRole('ROLE_OPERARIO') or hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> ActualizarInfoUsuario(@PathVariable("id_usuario") UUID id_usuario,
                                                   @RequestBody UpdateUsuarioInfoRequest updateUsuarioInfoRequest) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_IDUsuario(id_usuario);

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            usuario.setNombreUsuario(updateUsuarioInfoRequest.getNombreUsuario());
            usuario.setApellidoUsuario(updateUsuarioInfoRequest.getApellidoUsuario());
            usuario.setTelefonoUsuario(updateUsuarioInfoRequest.getTelefonoUsuario());

            usuarioService.GuardarUsuario(usuario);

            return new ResponseEntity<>(new MessageResponse("Se actualizó la Información del Usuario satisfactoriamente."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Usuario."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/usuario/{id_usuario}/profile/update/photo")
    @PreAuthorize("hasRole('ROLE_OPERARIO') or hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> ActualizarFotoUsuario(@PathVariable("id_usuario") UUID id_usuario,
                                                   @RequestPart("foto") MultipartFile foto) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_IDUsuario(id_usuario);

        if (usuario_data.isPresent()) {
            Usuario usuario = usuario_data.get();

            Optional<Imagen> foto_data =
                    imagenService.BuscarImagen_By_IDImagen(usuario.getImagenUsuario().getIdImagen());

            if (foto_data.isPresent()) {
                Imagen foto_perfil = foto_data.get();

                return Code_AssignMethods.AssignFotoPerfil(foto, foto_perfil, imagenService);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la foto del Usuario."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Usuario."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
