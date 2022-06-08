package pe.upc.mckchain.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.request.AnalisisLeyRequest;
import pe.upc.mckchain.dto.response.AnalisisLeyResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.dto.response.ubicacion.AnalisisLeySmartContractResponse;
import pe.upc.mckchain.model.AnalisisLey;
import pe.upc.mckchain.model.Pedido;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.service.IAnalisisLeyService;
import pe.upc.mckchain.service.IMineralService;
import pe.upc.mckchain.service.IPedidoService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.validations.AnalisisLeyValidation;

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
public class AnalisisLeyController {

    final
    IAnalisisLeyService analisisLeyService;

    final
    IPedidoService pedidoService;

    final
    IMineralService mineralService;

    final
    IUsuarioService usuarioService;

    public AnalisisLeyController(IAnalisisLeyService analisisLeyService, IPedidoService pedidoService,
                                 IMineralService mineralService, IUsuarioService usuarioService) {
        this.analisisLeyService = analisisLeyService;
        this.pedidoService = pedidoService;
        this.mineralService = mineralService;
        this.usuarioService = usuarioService;
    }

    @PostMapping("/encargadolaboratorio/{id_encargadolaboratorio}/analisisley/save")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> RegistrarAnalisisLey(@PathVariable("id_encargadolaboratorio") UUID id_encargadolaboratorio,
                                                  @RequestBody AnalisisLeyRequest analisisLeyRequest) {

        Optional<Usuario> encargadolaboratorio_data = usuarioService.BuscarUsuario_By_IDUsuario(id_encargadolaboratorio);

        if (encargadolaboratorio_data.isPresent()) {
            Usuario encargadolaboratorio = encargadolaboratorio_data.get();

            UUID idPedido = UUID.fromString(analisisLeyRequest.getPedido());

            Set<AnalisisLeyValidation> list_analisisley_to_validate = new HashSet<>();

            analisisLeyService.ValidarAnalisisLey(encargadolaboratorio.getIdUsuario(), idPedido)
                    .forEach(analisisLey -> list_analisisley_to_validate.add(new AnalisisLeyValidation(
                            encargadolaboratorio.getIdUsuario(),
                            idPedido
                    )));

            if (list_analisisley_to_validate.size() < 1) {
                Optional<Pedido> pedido_data = pedidoService.BuscarPedido_By_IDPedido(idPedido);

                if (pedido_data.isPresent()) {
                    Pedido pedido = pedido_data.get();

                    //Asignando Fecha de Registro Actual
                    ZoneId zona_actual = ZoneId.of("America/Lima");
                    LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

                    AnalisisLey analisisley = new AnalisisLey(
                            pedido,
                            encargadolaboratorio,
                            analisisLeyRequest.getPesoAnalisisLey(),
                            analisisLeyRequest.getDistribuciongranulometricaAnalisisLey(),
                            analisisLeyRequest.getHumedadAnalisisLey(),
                            analisisLeyRequest.getDensidadpulpaAnalisisLey(),
                            analisisLeyRequest.getCaudalAnalisisLey(),
                            fechahora_actual,
                            "HABILITADO");

                    analisisLeyService.GuardarAnalisisLey(analisisley);

                    pedido.setAnalisisleyPedido(analisisley);
                    pedido.setEstadoPedido("CULMINADO");
                    pedidoService.GuardarPedido(pedido);

                    return new ResponseEntity<>(new MessageResponse("Se registró correctamente el Análisis de Ley."),
                            HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del " +
                            "Pedido."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ya se atendió dicho Pedido."),
                        HttpStatus.CONFLICT);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Encargado de " +
                    "Laboratorio."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/encargadolaboratorio/{id_encargadolaboratorio}/analisisley/show/enable")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> MostrarAnalisisLeyHabilitadas(
            @PathVariable("id_encargadolaboratorio") UUID id_encargadolaboratorio) {

        Optional<Usuario> encargadolaboratorio_data = usuarioService.BuscarUsuario_By_IDUsuario(id_encargadolaboratorio);

        if (encargadolaboratorio_data.isPresent()) {
            Usuario encargadolaboratorio = encargadolaboratorio_data.get();

            Set<AnalisisLeyResponse> list_analisisley = new HashSet<>();

            analisisLeyService.BuscarAnalisisLey_By_IDMineraAndEstadoAnalisisLey(
                    encargadolaboratorio.getMineraUsuario().getIdUsuario(),
                    "HABILITADO"
            ).forEach(analisisLey -> list_analisisley.add(new AnalisisLeyResponse(
                    analisisLey.getIdAnalisisLey(),
                    SendFechaRegistroAnalisisLey(analisisLey.getFecharegistroAnalisisLey()),
                    analisisLey.getPedidoAnalisisLey().getMineralPedido().getNombreMineral(),
                    analisisLey.getPedidoAnalisisLey().getIdPedido(),
                    analisisLey.getPesoAnalisisLey(),
                    analisisLey.getDistribuciongranulometricaAnalisisLey(),
                    analisisLey.getHumedadAnalisisLey(),
                    analisisLey.getDensidadpulpaAnalisisLey(),
                    analisisLey.getCaudalAnalisisLey()
            )));

            return new ResponseEntity<>(list_analisisley,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Encargado de " +
                    "Laboratorio."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/encargadolaboratorio/{id_encargadolaboratorio}/analisisley/show/disable")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> MostrarAnalisisLeyInhabilitadas(
            @PathVariable("id_encargadolaboratorio") UUID id_encargadolaboratorio) {

        Optional<Usuario> encargadolaboratorio_data = usuarioService.BuscarUsuario_By_IDUsuario(id_encargadolaboratorio);

        if (encargadolaboratorio_data.isPresent()) {
            Usuario encargadolaboratorio = encargadolaboratorio_data.get();

            Set<AnalisisLeyResponse> list_analisisley = new HashSet<>();

            analisisLeyService.BuscarAnalisisLey_By_IDMineraAndEstadoAnalisisLey(
                    encargadolaboratorio.getMineraUsuario().getIdUsuario(),
                    "INHABILITADO"
            ).forEach(analisisLey -> list_analisisley.add(new AnalisisLeyResponse(
                    analisisLey.getIdAnalisisLey(),
                    SendFechaRegistroAnalisisLey(analisisLey.getFecharegistroAnalisisLey()),
                    analisisLey.getPedidoAnalisisLey().getMineralPedido().getNombreMineral(),
                    analisisLey.getPedidoAnalisisLey().getIdPedido(),
                    analisisLey.getPesoAnalisisLey(),
                    analisisLey.getDistribuciongranulometricaAnalisisLey(),
                    analisisLey.getHumedadAnalisisLey(),
                    analisisLey.getDensidadpulpaAnalisisLey(),
                    analisisLey.getCaudalAnalisisLey()
            )));

            return new ResponseEntity<>(list_analisisley,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Encargado de " +
                    "Laboratorio."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/analisisley/{id_analisisley}/update")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> ActualizarAnalisisLey(@PathVariable("id_analisisley") UUID id_analisisley,
                                                   @RequestBody AnalisisLeyRequest analisisLeyRequest) {

        Optional<AnalisisLey> analisisley_data = analisisLeyService.BuscarAnalisisLey_By_IDAnalisisLey(id_analisisley);

        if (analisisley_data.isPresent()) {
            AnalisisLey analisisley = analisisley_data.get();

            analisisley.setPesoAnalisisLey(analisisLeyRequest.getPesoAnalisisLey());
            analisisley.setDistribuciongranulometricaAnalisisLey(analisisLeyRequest.getDistribuciongranulometricaAnalisisLey());
            analisisley.setHumedadAnalisisLey(analisisLeyRequest.getHumedadAnalisisLey());
            analisisley.setDensidadpulpaAnalisisLey(analisisLeyRequest.getDensidadpulpaAnalisisLey());
            analisisley.setCaudalAnalisisLey(analisisLeyRequest.getCaudalAnalisisLey());

            analisisLeyService.GuardarAnalisisLey(analisisley);

            return new ResponseEntity<>(new MessageResponse("Se actualizó correctamente el Análisis de Ley."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Análisis de " +
                    "Ley."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/analisisley/{id_analisisley}/disable")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> InhabilitarAnalisisLey(@PathVariable("id_analisisley") UUID id_analisisley) {

        Optional<AnalisisLey> analisisley_data = analisisLeyService.BuscarAnalisisLey_By_IDAnalisisLey(id_analisisley);

        if (analisisley_data.isPresent()) {
            AnalisisLey analisisley = analisisley_data.get();

            analisisley.setEstadoAnalisisLey("INHABILITADO");

            analisisLeyService.GuardarAnalisisLey(analisisley);

            Optional<Pedido> pedido_data = pedidoService.BuscarPedido_By_IDPedido(
                    analisisley.getPedidoAnalisisLey().getIdPedido());

            if (pedido_data.isPresent()) {
                Pedido pedido = pedido_data.get();

                pedido.setEstadoPedido("PROCESANDO");

                pedidoService.GuardarPedido(pedido);

                return new ResponseEntity<>(new MessageResponse("Se inhabilitó correctamente el Análisis de Ley."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Pedido " +
                        "de Estudio del Comprador."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Análisis de " +
                    "Ley."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/analisisley/{id_analisisley}/enable")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> HabilitarAnalisisLey(@PathVariable("id_analisisley") UUID id_analisisley) {

        Optional<AnalisisLey> analisisley_data = analisisLeyService.BuscarAnalisisLey_By_IDAnalisisLey(id_analisisley);

        if (analisisley_data.isPresent()) {
            AnalisisLey analisisley = analisisley_data.get();

            analisisley.setEstadoAnalisisLey("HABILITADO");

            analisisLeyService.GuardarAnalisisLey(analisisley);

            Optional<Pedido> pedido_data = pedidoService.BuscarPedido_By_IDPedido(
                    analisisley.getPedidoAnalisisLey().getIdPedido());

            if (pedido_data.isPresent()) {
                Pedido pedido = pedido_data.get();

                pedido.setEstadoPedido("CULMINADO");

                pedidoService.GuardarPedido(pedido);

                return new ResponseEntity<>(new MessageResponse("Se habilitó correctamente el Análisis de Ley."),
                        HttpStatus.ACCEPTED);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Pedido " +
                        "de Estudio del Comprador."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Análisis de " +
                    "Ley."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/analisisley/{id_analisisley}/show/detail")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarDetalleAnalisisLey(@PathVariable("id_analisisley") UUID id_analisisley) {

        Optional<AnalisisLey> analisisley_data = analisisLeyService.BuscarAnalisisLey_By_IDAnalisisLey(id_analisisley);

        if (analisisley_data.isPresent()) {
            AnalisisLey analisisley = analisisley_data.get();

            return new ResponseEntity<>(
                    new AnalisisLeyResponse(
                            analisisley.getIdAnalisisLey(),
                            SendFechaRegistroAnalisisLey(analisisley.getFecharegistroAnalisisLey()),
                            analisisley.getPedidoAnalisisLey().getMineralPedido().getNombreMineral(),
                            analisisley.getPedidoAnalisisLey().getIdPedido(),
                            analisisley.getPesoAnalisisLey(),
                            analisisley.getDistribuciongranulometricaAnalisisLey(),
                            analisisley.getHumedadAnalisisLey(),
                            analisisley.getDensidadpulpaAnalisisLey(),
                            analisisley.getCaudalAnalisisLey(),
                            analisisley.getPedidoAnalisisLey().getCompradorPedido().getNombreUsuario() + " " +
                                    analisisley.getPedidoAnalisisLey().getCompradorPedido().getApellidoUsuario(),
                            analisisley.getPedidoAnalisisLey().getCompradorPedido().getRucUsuario(),
                            analisisley.getEncargadolaboratorioAnalisisLey().getNombreUsuario() + " " +
                                    analisisley.getEncargadolaboratorioAnalisisLey().getApellidoUsuario(),
                            analisisley.getEncargadolaboratorioAnalisisLey().getMineraUsuario().getNombreUsuario(),
                            analisisley.getEncargadolaboratorioAnalisisLey().getMineraUsuario().getRucUsuario()
                    ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Análisis de " +
                    "Ley."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/analisisley/{id_analisisley}/detail/send/smartcontract")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> EnviarAnalisisLeytoSmartContract(@PathVariable("id_analisisley") UUID id_analisisley) throws JsonProcessingException {
        Optional<AnalisisLey> analisisley_data = analisisLeyService.BuscarAnalisisLey_By_IDAnalisisLey(id_analisisley);

        if (analisisley_data.isPresent()) {
            AnalisisLey analisisley = analisisley_data.get();

            AnalisisLeyResponse analisisleyresponse = new AnalisisLeyResponse(
                    analisisley.getIdAnalisisLey(),
                    SendFechaRegistroAnalisisLey(analisisley.getFecharegistroAnalisisLey()),
                    analisisley.getPedidoAnalisisLey().getMineralPedido().getNombreMineral(),
                    analisisley.getPedidoAnalisisLey().getIdPedido(),
                    analisisley.getPesoAnalisisLey(),
                    analisisley.getDistribuciongranulometricaAnalisisLey(),
                    analisisley.getHumedadAnalisisLey(),
                    analisisley.getDensidadpulpaAnalisisLey(),
                    analisisley.getCaudalAnalisisLey(),
                    analisisley.getPedidoAnalisisLey().getCompradorPedido().getNombreUsuario() + " " +
                            analisisley.getPedidoAnalisisLey().getCompradorPedido().getApellidoUsuario(),
                    analisisley.getPedidoAnalisisLey().getCompradorPedido().getRucUsuario(),
                    analisisley.getEncargadolaboratorioAnalisisLey().getNombreUsuario() + " " +
                            analisisley.getEncargadolaboratorioAnalisisLey().getApellidoUsuario(),
                    analisisley.getEncargadolaboratorioAnalisisLey().getMineraUsuario().getNombreUsuario(),
                    analisisley.getEncargadolaboratorioAnalisisLey().getMineraUsuario().getRucUsuario()
            );

            ObjectWriter objectWriter = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String trama_smartcontract = objectWriter.writeValueAsString(analisisleyresponse);

            return new ResponseEntity<>(
                    new AnalisisLeySmartContractResponse(
                            trama_smartcontract
                    ),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Análisis de " +
                    "Ley."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private String SendFechaRegistroAnalisisLey(LocalDateTime fecharegistroAnalisisLey) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return fecharegistroAnalisisLey.format(formatter);
    }
}
