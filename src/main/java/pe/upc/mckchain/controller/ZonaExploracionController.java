package pe.upc.mckchain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.request.ZonaExploracionRequest;
import pe.upc.mckchain.dto.response.ZonaExploracionResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.ZonaExploracion;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IZonaExploracionService;
import pe.upc.mckchain.validations.ZonaExploracionValidation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ZonaExploracionController {

    final
    IZonaExploracionService zonaExploracionService;

    final
    IUsuarioService usuarioService;

    public ZonaExploracionController(IZonaExploracionService zonaExploracionService, IUsuarioService usuarioService) {
        this.zonaExploracionService = zonaExploracionService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/minera/{id_minera}/zonaexploracion/save")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> RegistrarZonaExploracion(@PathVariable("id_minera") UUID id_minera,
                                                      @RequestBody ZonaExploracionRequest zonaExploracionRequest) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Set<ZonaExploracionValidation> list_zonasexploracion_to_validate = new HashSet<>();

            zonaExploracionService.ValidarZonasExploracion(
                            id_minera,
                            zonaExploracionRequest.getNombreZonaExploracion())
                    .forEach(zonaexploracion -> list_zonasexploracion_to_validate.add(new ZonaExploracionValidation(
                            id_minera,
                            zonaExploracionRequest.getNombreZonaExploracion()
                    )));

            if (list_zonasexploracion_to_validate.size() < 1) {
                //Asignando Fecha de Registro Actual
                ZoneId zona_actual = ZoneId.of("America/Lima");
                LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

                ZonaExploracion zonaexploracion = new ZonaExploracion(
                        zonaExploracionRequest.getNombreZonaExploracion(),
                        zonaExploracionRequest.getDescripcionZonaExploracion(),
                        "HABILITADO",
                        fechahora_actual,
                        minera
                );

                zonaExploracionService.GuardarZonaExploracion(zonaexploracion);

                return new ResponseEntity<>(new MessageResponse("Se registró correctamente la Zona de Exploración."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ya se encuentra registrado esa Zona de Exploración."),
                        HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/minera/{id_minera}/zonasexploracion/show/enable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> MostrarZonasExploracionHabilitadas(@PathVariable("id_minera") UUID id_minera) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Set<ZonaExploracionResponse> list_zonasexploracion = new HashSet<>();

            zonaExploracionService.BuscarZonasExploracion_By_IDMineraAndEstadoZonaExploracion(minera.getIdUsuario(),
                            "HABILITADO")
                    .forEach(zonaexploracion -> list_zonasexploracion.add(new ZonaExploracionResponse(
                            zonaexploracion.getIdZonaExploracion(),
                            zonaexploracion.getNombreZonaExploracion(),
                            zonaexploracion.getDescripcionZonaExploracion(),
                            SendFechaRegistroZonaExploracion(zonaexploracion.getFecharegistroZonaExploracion())
                    )));

            return new ResponseEntity<>(list_zonasexploracion,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/minera/{id_minera}/zonasexploracion/show/disable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> MostrarZonasExploracionInhabilitadas(@PathVariable("id_minera") UUID id_minera) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Set<ZonaExploracionResponse> list_zonasexploracion = new HashSet<>();

            zonaExploracionService.BuscarZonasExploracion_By_IDMineraAndEstadoZonaExploracion(minera.getIdUsuario(),
                            "INHABILITADO")
                    .forEach(zonaexploracion -> list_zonasexploracion.add(new ZonaExploracionResponse(
                            zonaexploracion.getIdZonaExploracion(),
                            zonaexploracion.getNombreZonaExploracion(),
                            zonaexploracion.getDescripcionZonaExploracion(),
                            SendFechaRegistroZonaExploracion(zonaexploracion.getFecharegistroZonaExploracion())
                    )));

            return new ResponseEntity<>(list_zonasexploracion,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private String SendFechaRegistroZonaExploracion(LocalDateTime fecharegistroZonaExploracion) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return fecharegistroZonaExploracion.format(formatter);
    }

    @PutMapping("/zonaexploracion/{id_zonaexploracion}/update")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> ActualizarZonaExploracion(@PathVariable("id_zonaexploracion") UUID id_zonaexploracion,
                                                       @RequestBody ZonaExploracionRequest zonaExploracionRequest) {

        Optional<ZonaExploracion> zonaexploracion_data =
                zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(id_zonaexploracion);

        if (zonaexploracion_data.isPresent()) {
            ZonaExploracion zonaexploracion = zonaexploracion_data.get();

            zonaexploracion.setNombreZonaExploracion(zonaExploracionRequest.getNombreZonaExploracion());
            zonaexploracion.setDescripcionZonaExploracion(zonaexploracion.getDescripcionZonaExploracion());

            zonaExploracionService.GuardarZonaExploracion(zonaexploracion);

            return new ResponseEntity<>(new MessageResponse("Se actualizó correctamente la Zona de Exploración."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Zona de " +
                    "Exploración."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/zonaexploracion/{id_zonaexploracion}/disable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> InhabilitarZonaExploracion(@PathVariable("id_zonaexploracion") UUID id_zonaexploracion) {

        Optional<ZonaExploracion> zonaexploracion_data =
                zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(id_zonaexploracion);

        if (zonaexploracion_data.isPresent()) {
            ZonaExploracion zonaexploracion = zonaexploracion_data.get();

            zonaexploracion.setEstadoZonaExploracion("INHABILITADO");

            zonaExploracionService.GuardarZonaExploracion(zonaexploracion);

            return new ResponseEntity<>(new MessageResponse("Se inhabilitó correctamente la Zona de Exploración."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Zona de " +
                    "Exploración."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/zonaexploracion/{id_zonaexploracion}/enable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> HabilitarZonaExploracion(@PathVariable("id_zonaexploracion") UUID id_zonaexploracion) {

        Optional<ZonaExploracion> zonaexploracion_data =
                zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(id_zonaexploracion);

        if (zonaexploracion_data.isPresent()) {
            ZonaExploracion zonaexploracion = zonaexploracion_data.get();

            zonaexploracion.setEstadoZonaExploracion("HABILITADO");

            zonaExploracionService.GuardarZonaExploracion(zonaexploracion);

            return new ResponseEntity<>(new MessageResponse("Se habilitó correctamente la Zona de Exploración."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Zona de " +
                    "Exploración."),
                    HttpStatus.NOT_FOUND);
        }
    }
}
