package pe.upc.mckchain.controller.usuario.profile;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pe.upc.mckchain.controller.util.util_code.Code_UploadImage;
import pe.upc.mckchain.dto.request.usuario.profile.UpdateMineraInfoRequest;
import pe.upc.mckchain.dto.response.general.ImagenResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.dto.response.profile.MineraProfileResponse;
import pe.upc.mckchain.model.*;
import pe.upc.mckchain.service.*;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ProfileMineraController {

    final
    IUsuarioService usuarioService;

    final
    IMineraService mineraService;

    final
    IImagenService imagenService;

    final
    IDepartamentoService departamentoService;

    final
    IProvinciaService provinciaService;

    final
    IDistritoService distritoService;

    public ProfileMineraController(IUsuarioService usuarioService, IMineraService mineraService,
                                   IImagenService imagenService, IDepartamentoService departamentoService,
                                   IProvinciaService provinciaService, IDistritoService distritoService) {
        this.usuarioService = usuarioService;
        this.mineraService = mineraService;
        this.imagenService = imagenService;
        this.departamentoService = departamentoService;
        this.provinciaService = provinciaService;
        this.distritoService = distritoService;
    }

    @GetMapping("/minera/{id_minera}/profile")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> MineraProfile(@PathVariable("id_minera") UUID id_minera) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Optional<Minera> mineradata_data = mineraService.BuscarMinera_By_IDUsuarioMinera(minera.getIdUsuario());

            if (mineradata_data.isPresent()) {
                Minera mineradata = mineradata_data.get();

                Distrito distrito = mineradata.getDistritoMinera();
                Provincia provincia = SendProvinciaByDistrito(distrito, provinciaService);
                Departamento departamento = SendDepartamentoByProvincia(provincia, departamentoService);

                String ubicacion = "Distrito de " + distrito.getNombreDistrito() + ", " + provincia.getNombreProvincia()
                        + " - " + departamento.getNombreDepartamento();

                return new ResponseEntity<>(new MineraProfileResponse(
                        minera.getIdUsuario(),
                        minera.getRucUsuario(),
                        mineradata.getCodigounicoMinera(),
                        ubicacion,
                        minera.getNombreUsuario(),
                        minera.getTelefonoUsuario(),
                        minera.getEmailUsuario(),
                        minera.getWebUsuario(),
                        new ImagenResponse(
                                minera.getImagenUsuario().getNombreImagen(),
                                minera.getImagenUsuario().getUrlImagen()
                        )
                ),
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de REINFO de" +
                        "dicha Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/minera/{id_minera}/profile/update/info")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> ActualizarInfoMinera(@PathVariable("id_minera") UUID id_minera,
                                                  @RequestBody UpdateMineraInfoRequest updateMineraInfoRequest) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            minera.setTelefonoUsuario(updateMineraInfoRequest.getTelefonoUsuario());
            minera.setWebUsuario(updateMineraInfoRequest.getWebUsuario());

            usuarioService.GuardarUsuario(minera);

            return new ResponseEntity<>(new MessageResponse("Se actualizó la Información de la Minera satisfactoriamente."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/minera/{id_minera}/profile/update/logo")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> ActualizarLogoMinera(@PathVariable("id_minera") UUID id_minera,
                                                  @RequestPart("logo") MultipartFile logo) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Optional<Imagen> logo_data =
                    imagenService.BuscarImagen_By_IDImagen(minera.getImagenUsuario().getIdImagen());

            if (logo_data.isPresent()) {
                Imagen logo_minera = logo_data.get();

                try {
                    if (!logo.isEmpty()) {
                        //Obteniendo Extension del Archivo Seleccionado
                        String separador_logo = Pattern.quote(".");
                        String[] formato_logo = Objects.requireNonNull(logo.getOriginalFilename()).split(separador_logo);

                        String nuevonombre_logo = Code_UploadImage.UpdateImageName(logo_minera.getNombreImagen())
                                + "." + formato_logo[formato_logo.length - 1];

                        String url_logo = Code_UploadImage.SendFileUrl("/logos/", nuevonombre_logo);

                        logo_minera.setNombreImagen(nuevonombre_logo);
                        logo_minera.setUrlImagen(url_logo);
                        logo_minera.setArchivoImagen(logo.getBytes());
                        logo_minera.setTipoarchivoImagen(logo.getContentType());

                        imagenService.GuardarImagen(logo_minera);

                        return new ResponseEntity<>(new MessageResponse("Se ha actualizado su logo satisfactoriamente."),
                                HttpStatus.ACCEPTED);
                    } else {
                        return new ResponseEntity<>(new MessageResponse("No se ha seleccionado un archivo."),
                                HttpStatus.BAD_REQUEST);
                    }
                } catch (Exception e) {
                    return new ResponseEntity<>(new MessageResponse("No se puede subir el archivo " + e),
                            HttpStatus.EXPECTATION_FAILED);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el logo de la Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private Provincia SendProvinciaByDistrito(Distrito distrito, IProvinciaService provinciaService) {

        Optional<Provincia> provincia_data = provinciaService.BuscarProvincia_By_IDDistrito(distrito.getIdDistrito());

        return provincia_data.orElse(null);
    }

    private Departamento SendDepartamentoByProvincia(Provincia provincia, IDepartamentoService departamentoService) {

        Optional<Departamento> departamento_data =
                departamentoService.BuscarDepartamento_By_IDProvincia(provincia.getIdProvincia());

        return departamento_data.orElse(null);
    }
}
