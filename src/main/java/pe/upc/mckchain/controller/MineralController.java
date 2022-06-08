package pe.upc.mckchain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.request.FaseMineralRequest;
import pe.upc.mckchain.dto.request.MineralRequest;
import pe.upc.mckchain.dto.response.MineralResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Mineral;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.ZonaExploracion;
import pe.upc.mckchain.service.IMineralService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IZonaExploracionService;
import pe.upc.mckchain.validations.MineralValidation;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class MineralController {

    final
    IMineralService mineralService;

    final
    IZonaExploracionService zonaExploracionService;

    final
    IUsuarioService usuarioService;

    public MineralController(IMineralService mineralService, IZonaExploracionService zonaExploracionService,
                             IUsuarioService usuarioService) {
        this.mineralService = mineralService;
        this.zonaExploracionService = zonaExploracionService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/minera/{id_minera}/mineral/save")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> RegistrarMineral(@PathVariable("id_minera") UUID id_minera,
                                              @RequestBody MineralRequest mineralRequest) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            UUID id_zonaexploracion = UUID.fromString(mineralRequest.getZonaexploracionMineral());

            Optional<ZonaExploracion> zonaexploracion_data =
                    zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(
                            id_zonaexploracion);

            if (zonaexploracion_data.isPresent()) {
                ZonaExploracion zonaexploracion = zonaexploracion_data.get();

                Set<MineralValidation> list_minerales_to_validate = new HashSet<>();

                mineralService.ValidarMinerales(
                                zonaexploracion.getIdZonaExploracion(),
                                mineralRequest.getNombreMineral())
                        .forEach(mineral -> list_minerales_to_validate.add(new MineralValidation(
                                zonaexploracion.getIdZonaExploracion(),
                                mineralRequest.getNombreMineral()
                        )));

                if (list_minerales_to_validate.size() < 1) {
                    //Formateando Fecha de Nacimiento
                    String pattern = "dd-MM-yyyy";
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                    LocalDate fecha_extraccion = LocalDate.parse(mineralRequest.getFechaextraccionMineral(),
                            formatter);

                    Mineral mineral = new Mineral(
                            fecha_extraccion,
                            mineralRequest.getNombreMineral(),
                            mineralRequest.getTipomuestraMineral(),
                            "HABILITADO",
                            "EXPLORACION",
                            zonaexploracion,
                            true
                    );

                    mineralService.GuardarMineral(mineral);

                    return new ResponseEntity<>(new MessageResponse("Se registró correctamente el Mineral."),
                            HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ya se encuentra registrado ese Mineral."),
                            HttpStatus.CONFLICT);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Zona " +
                        "de Exploración."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/minera/{id_minera}/mineral/show/enable")
    @PreAuthorize("hasRole('ROLE_MINERA') or hasRole('ROLE_OPERARIO')")
    public ResponseEntity<?> MostrarMineralesHabilitados(@PathVariable("id_minera") UUID id_minera) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Set<MineralResponse> list_mineral = new HashSet<>();

            mineralService.BuscarMinerales_By_IDMineraAndEstadoMineral(minera.getIdUsuario(),
                            "HABILITADO")
                    .forEach(mineral -> list_mineral.add(new MineralResponse(
                            mineral.getIdMineral(),
                            mineral.getNombreMineral(),
                            mineral.getTipomuestraMineral(),
                            mineral.getFechaextraccionMineral(),
                            mineral.getFaseMineral(),
                            mineral.getZonaexploracionMineral().getIdZonaExploracion(),
                            mineral.getZonaexploracionMineral().getNombreZonaExploracion()
                    )));

            return new ResponseEntity<>(list_mineral,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/minera/{id_minera}/mineral/show/disable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> MostrarMineralesInhabilitados(@PathVariable("id_minera") UUID id_minera) {

        Optional<Usuario> minera_data = usuarioService.BuscarUsuario_By_IDUsuario(id_minera);

        if (minera_data.isPresent()) {
            Usuario minera = minera_data.get();

            Set<MineralResponse> list_mineral = new HashSet<>();

            mineralService.BuscarMinerales_By_IDMineraAndEstadoMineral(minera.getIdUsuario(),
                            "INHABILITADO")
                    .forEach(mineral -> list_mineral.add(new MineralResponse(
                            mineral.getIdMineral(),
                            mineral.getNombreMineral(),
                            mineral.getTipomuestraMineral(),
                            mineral.getFechaextraccionMineral(),
                            mineral.getFaseMineral(),
                            mineral.getZonaexploracionMineral().getIdZonaExploracion(),
                            mineral.getZonaexploracionMineral().getNombreZonaExploracion()
                    )));

            return new ResponseEntity<>(list_mineral,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/mineral/{id_mineral}/update")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> ActualizarMineral(@PathVariable("id_mineral") UUID id_mineral,
                                               @RequestBody MineralRequest mineralRequest) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(id_mineral);

        if (mineral_data.isPresent()) {
            Mineral mineral = mineral_data.get();

            UUID id_zonaexploracion = UUID.fromString(mineralRequest.getZonaexploracionMineral());

            Optional<ZonaExploracion> zonaexploracion_data =
                    zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(
                            id_zonaexploracion);

            if (zonaexploracion_data.isPresent()) {
                ZonaExploracion zonaexploracion = zonaexploracion_data.get();

                //Formateando Fecha de Nacimiento
                String pattern = "dd-MM-yyyy";
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
                LocalDate fecha_extraccion = LocalDate.parse(mineralRequest.getFechaextraccionMineral(),
                        formatter);

                mineral.setZonaexploracionMineral(zonaexploracion);
                mineral.setFechaextraccionMineral(fecha_extraccion);
                mineral.setNombreMineral(mineralRequest.getNombreMineral());
                mineral.setTipomuestraMineral(mineralRequest.getTipomuestraMineral());

                mineralService.GuardarMineral(mineral);

                return new ResponseEntity<>(new MessageResponse("Se actualizó correctamente el Mineral."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Zona " +
                        "de Exploración."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Mineral."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/mineral/{id_mineral}/disable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> InhabilitarMineral(@PathVariable("id_mineral") UUID id_mineral) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(id_mineral);

        if (mineral_data.isPresent()) {
            Mineral mineral = mineral_data.get();

            mineral.setEstadoMineral("INHABILITADO");

            mineralService.GuardarMineral(mineral);

            return new ResponseEntity<>(new MessageResponse("Se inhabilitó correctamente el Mineral."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Mineral."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/mineral/{id_mineral}/enable")
    @PreAuthorize("hasRole('ROLE_MINERA')")
    public ResponseEntity<?> HabilitarMineral(@PathVariable("id_mineral") UUID id_mineral) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(id_mineral);

        if (mineral_data.isPresent()) {
            Mineral mineral = mineral_data.get();

            mineral.setEstadoMineral("HABILITADO");

            mineralService.GuardarMineral(mineral);

            return new ResponseEntity<>(new MessageResponse("Se habilitó correctamente el Mineral."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Mineral."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/mineral/{id_mineral}/phase/update")
    @PreAuthorize("hasRole('ROLE_OPERARIO')")
    public ResponseEntity<?> ActualizarFaseMineral(@PathVariable("id_mineral") UUID id_mineral,
                                                   @RequestBody FaseMineralRequest faseMineralRequest) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(id_mineral);

        if (mineral_data.isPresent()) {
            Mineral mineral = mineral_data.get();

            mineral.setFaseMineral(faseMineralRequest.getFaseMineral());

            mineralService.GuardarMineral(mineral);

            return new ResponseEntity<>(new MessageResponse("Se actualizó correctamente la Fase del Mineral."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Mineral."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/mineral/show/all")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarMineralesToComprador() {

        Set<MineralResponse> list_minerales = new HashSet<>();

        mineralService.MostrarMineralesToPedido()
                .forEach(mineral -> list_minerales.add(new MineralResponse(
                        mineral.getIdMineral(),
                        mineral.getNombreMineral(),
                        mineral.getTipomuestraMineral(),
                        mineral.getFechaextraccionMineral(),
                        mineral.getFaseMineral(),
                        mineral.getZonaexploracionMineral().getMineraZonaExploracion().getIdUsuario(),
                        mineral.getZonaexploracionMineral().getMineraZonaExploracion().getNombreUsuario(),
                        mineral.getZonaexploracionMineral().getMineraZonaExploracion().getRucUsuario()
                )));

        return new ResponseEntity<>(list_minerales,
                HttpStatus.OK);
    }
}
