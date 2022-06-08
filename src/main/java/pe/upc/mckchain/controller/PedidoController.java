package pe.upc.mckchain.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.request.PedidoRequest;
import pe.upc.mckchain.dto.response.PedidoResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Mineral;
import pe.upc.mckchain.model.Pedido;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.ZonaExploracion;
import pe.upc.mckchain.service.IMineralService;
import pe.upc.mckchain.service.IPedidoService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IZonaExploracionService;
import pe.upc.mckchain.validations.PedidoValidation;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class PedidoController {

    final
    IPedidoService pedidoService;

    final
    IMineralService mineralService;

    final
    IUsuarioService usuarioService;

    final
    IZonaExploracionService zonaExploracionService;

    public PedidoController(IPedidoService pedidoService, IMineralService mineralService, IUsuarioService usuarioService, IZonaExploracionService zonaExploracionService) {
        this.pedidoService = pedidoService;
        this.mineralService = mineralService;
        this.usuarioService = usuarioService;
        this.zonaExploracionService = zonaExploracionService;
    }

    @PostMapping("/comprador/{id_comprador}/pedido/save")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> RegistrarPedido(@PathVariable("id_comprador") UUID id_comprador,
                                             @RequestBody PedidoRequest pedidoRequest) {

        Set<PedidoValidation> list_pedidos_to_validate = new HashSet<>();

        UUID idMineral = UUID.fromString(pedidoRequest.getMineral());

        pedidoService.ValidarPedidos(
                        id_comprador,
                        idMineral
                )
                .forEach(pedido -> list_pedidos_to_validate.add(new PedidoValidation(
                        id_comprador,
                        idMineral
                )));

        if (list_pedidos_to_validate.size() < 1) {
            Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

            if (comprador_data.isPresent()) {
                Usuario comprador = comprador_data.get();

                Optional<Mineral> mineral_data = mineralService.BuscarMineral_By_IDMineral(idMineral);

                if (mineral_data.isPresent()) {
                    Mineral mineral = mineral_data.get();

                    //Asignando Fecha de Registro Actual
                    ZoneId zona_actual = ZoneId.of("America/Lima");
                    LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

                    Pedido pedido = new Pedido(
                            comprador,
                            "PENDIENTE",
                            fechahora_actual
                    );

                    pedidoService.GuardarPedido(pedido);

                    mineral.setPedidoMineral(pedido);
                    mineral.setHabilitacionpedidoMineral(false);
                    mineralService.GuardarMineral(mineral);

                    return new ResponseEntity<>(new MessageResponse("Se registró correctamente el Pedido."),
                            HttpStatus.ACCEPTED);
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del " +
                            "Mineral seleccionado."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ya se encuentra registrado ese Pedido."),
                    HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/comprador/{id_comprador}/pedido/show/done")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarPedidosTerminados(@PathVariable("id_comprador") UUID id_comprador) {

        Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (comprador_data.isPresent()) {
            Usuario comprador = comprador_data.get();

            Set<PedidoResponse> list_pedidos = new HashSet<>();

            pedidoService.BuscarPedidos_By_IDCompradorAndEstadoPedido(comprador.getIdUsuario(),
                            "CULMINADO")
                    .forEach(pedido -> list_pedidos.add(new PedidoResponse(
                            pedido.getIdPedido(),
                            SendFechaPedido(pedido.getFechaPedido()),
                            SendMineralData(pedido).getIdMineral(),
                            SendMineralData(pedido).getNombreMineral(),
                            SendMineralData(pedido).getFaseMineral(),
                            Objects.requireNonNull(SendMineraData(pedido)).getNombreUsuario(),
                            Objects.requireNonNull(SendMineraData(pedido)).getRucUsuario(),
                            pedido.getAnalisisleyPedido().getIdAnalisisLey()
                    )));

            return new ResponseEntity<>(list_pedidos,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comprador/{id_comprador}/pedido/show/processing")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarPedidosEnProgreso(@PathVariable("id_comprador") UUID id_comprador) {

        Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (comprador_data.isPresent()) {
            Usuario comprador = comprador_data.get();

            Set<PedidoResponse> list_pedidos = new HashSet<>();

            pedidoService.BuscarPedidos_By_IDCompradorAndEstadoPedido(comprador.getIdUsuario(),
                            "PROCESANDO")
                    .forEach(pedido -> list_pedidos.add(new PedidoResponse(
                            pedido.getIdPedido(),
                            SendFechaPedido(pedido.getFechaPedido()),
                            SendMineralData(pedido).getIdMineral(),
                            SendMineralData(pedido).getNombreMineral(),
                            SendMineralData(pedido).getFaseMineral(),
                            Objects.requireNonNull(SendMineraData(pedido)).getNombreUsuario(),
                            Objects.requireNonNull(SendMineraData(pedido)).getRucUsuario()
                    )));

            return new ResponseEntity<>(list_pedidos,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/comprador/{id_comprador}/pedido/show/pending")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarPedidosPendientes(@PathVariable("id_comprador") UUID id_comprador) {

        Optional<Usuario> comprador_data = usuarioService.BuscarUsuario_By_IDUsuario(id_comprador);

        if (comprador_data.isPresent()) {
            Usuario comprador = comprador_data.get();

            Set<PedidoResponse> list_pedidos = new HashSet<>();

            pedidoService.BuscarPedidos_By_IDCompradorAndEstadoPedido(comprador.getIdUsuario(),
                            "PENDIENTE")
                    .forEach(pedido -> list_pedidos.add(new PedidoResponse(
                            pedido.getIdPedido(),
                            SendFechaPedido(pedido.getFechaPedido()),
                            SendMineralData(pedido).getIdMineral(),
                            SendMineralData(pedido).getNombreMineral(),
                            SendMineralData(pedido).getFaseMineral(),
                            Objects.requireNonNull(SendMineraData(pedido)).getNombreUsuario(),
                            Objects.requireNonNull(SendMineraData(pedido)).getRucUsuario()
                    )));

            return new ResponseEntity<>(list_pedidos,
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Comprador."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private Mineral SendMineralData(Pedido pedido) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(pedido.getMineralPedido().getIdMineral());

        return mineral_data.orElse(null);
    }

    private Usuario SendMineraData(Pedido pedido) {

        Optional<Mineral> mineral_data =
                mineralService.BuscarMineral_By_IDMineral(pedido.getMineralPedido().getIdMineral());

        if (mineral_data.isPresent()) {
            Mineral mineral = mineral_data.get();

            Optional<ZonaExploracion> zonaexploracion_data =
                    zonaExploracionService.BuscarZonaExploracion_By_IDZonaExploracion(mineral.getZonaexploracionMineral()
                            .getIdZonaExploracion());

            if (zonaexploracion_data.isPresent()) {
                ZonaExploracion zonaexploracion = zonaexploracion_data.get();

                Optional<Usuario> minera_data =
                        usuarioService.BuscarUsuario_By_IDUsuario(zonaexploracion.getMineraZonaExploracion()
                                .getIdUsuario());

                return minera_data.orElse(null);
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @PutMapping("/pedido/{id_pedido}/estado/update/processing")
    @PreAuthorize("hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> SolicitarEstudioFromPedidoMineral(@PathVariable("id_pedido") UUID id_pedido) {

        Optional<Pedido> pedido_data = pedidoService.BuscarPedido_By_IDPedido(id_pedido);

        if (pedido_data.isPresent()) {
            Pedido pedido = pedido_data.get();

            pedido.setEstadoPedido("PROCESANDO");

            pedidoService.GuardarPedido(pedido);

            return new ResponseEntity<>(new MessageResponse("Se actualizó correctamente el Estado del Pedido."),
                    HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Pedido."),
                    HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/encargadolaboratorio/{id_encargadolaboratorio}/pedidos/show/pending")
    @PreAuthorize("hasRole('ROLE_ENCARGADOLABORATORIO')")
    public ResponseEntity<?> MostrarPedidosToEncargadoLaboratorio(
            @PathVariable("id_encargadolaboratorio") UUID id_encargadolaboratorio) {

        Optional<Usuario> encargadolaboratorio_data = usuarioService.BuscarUsuario_By_IDUsuario(id_encargadolaboratorio);

        if (encargadolaboratorio_data.isPresent()) {
            Usuario encargadolaboratorio = encargadolaboratorio_data.get();

            Optional<Usuario> minera_data =
                    usuarioService.BuscarUsuario_By_IDUsuario(encargadolaboratorio.getMineraUsuario().getIdUsuario());

            if (minera_data.isPresent()) {
                Usuario minera = minera_data.get();

                Set<PedidoResponse> list_pedidos = new HashSet<>();

                pedidoService.BuscarPedidosToEncargadoLaboratorio(minera.getIdUsuario())
                        .forEach(pedido -> list_pedidos.add(new PedidoResponse(
                                pedido.getIdPedido(),
                                SendFechaPedido(pedido.getFechaPedido()),
                                pedido.getMineralPedido().getIdMineral(),
                                pedido.getMineralPedido().getNombreMineral(),
                                pedido.getMineralPedido().getFaseMineral(),
                                pedido.getCompradorPedido().getIdUsuario(),
                                pedido.getCompradorPedido().getNombreUsuario() + " " +
                                        pedido.getCompradorPedido().getApellidoUsuario()
                        )));

                return new ResponseEntity<>(list_pedidos,
                        HttpStatus.OK);
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información de la Minera."),
                        HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la información del Encargado de " +
                    "Laboratorio."),
                    HttpStatus.NOT_FOUND);
        }
    }

    private String SendFechaPedido(LocalDateTime fechaPedido) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

        return fechaPedido.format(formatter);
    }
}
