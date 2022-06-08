package pe.upc.mckchain.controller.usuario.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.upc.mckchain.controller.util.util_code.Code_AssignMethods;
import pe.upc.mckchain.dto.request.usuario.profile.UpdateCompradorInfoRequest;
import pe.upc.mckchain.dto.response.general.ImagenResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.dto.response.profile.CompradorProfileResponse;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.service.IImagenService;
import pe.upc.mckchain.service.IUsuarioService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProfileCompradorController {

    final
    IUsuarioService usuarioService;

    final
    IImagenService imagenService;

    public ProfileCompradorController(IUsuarioService usuarioService, IImagenService imagenService) {
        this.usuarioService = usuarioService;
        this.imagenService = imagenService;
    }

    @GetMapping("/comprador/{id_comprador}/profile")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> CompradorProfile(@PathVariable("id_comprador") UUID id_comprador) {

        Optional<Usuario> usuario_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (usuario_data.isPresent()) {
            Usuario comprador = usuario_data.get();

            return new ResponseEntity<>(new CompradorProfileResponse(
                    comprador.getIdUsuario(),
                    comprador.getRucUsuario(),
                    comprador.getNombreUsuario(),
                    comprador.getApellidoUsuario(),
                    comprador.getTelefonoUsuario(),
                    comprador.getEmailUsuario(),
                    comprador.getWebUsuario(),
                    new ImagenResponse(
                            comprador.getImagenUsuario().getNombreImagen(),
                            comprador.getImagenUsuario().getUrlImagen()
                    )
            ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comprador/{id_comprador}/profile/update/info")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> ActualizarInfoComprador(@PathVariable("id_comprador") UUID id_comprador,
                                                     @RequestBody UpdateCompradorInfoRequest updateCompradorInfoRequest) {

        Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (comprador_data.isPresent()) {
            Usuario comprador = comprador_data.get();

            comprador.setNombreUsuario(updateCompradorInfoRequest.getNombreUsuario());
            comprador.setApellidoUsuario(updateCompradorInfoRequest.getApellidoUsuario());
            comprador.setTelefonoUsuario(updateCompradorInfoRequest.getTelefonoUsuario());
            comprador.setWebUsuario(updateCompradorInfoRequest.getWebUsuario());

            usuarioService.GuardarUsuario(comprador);

            return new ResponseEntity<>(new MessageResponse("Se actualizó la Información del Comprador satisfactoriamente."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comprador/{id_comprador}/profile/update/photo")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> ActualizarFotoComprador(@PathVariable("id_comprador") UUID id_comprador,
                                                     @RequestPart("foto") MultipartFile foto) {

        Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (comprador_data.isPresent()) {
            Usuario comprador = comprador_data.get();

            Optional<Imagen> foto_data =
                    imagenService.BuscarImagen_By_IDImagen(comprador.getImagenUsuario().getIdImagen());

            if (foto_data.isPresent()) {
                Imagen foto_perfil = foto_data.get();

                return Code_AssignMethods.AssignFotoPerfil(foto, foto_perfil, imagenService);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la foto del Comprador."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
