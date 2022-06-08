package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Pedido;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IPedidoService {

    Set<Pedido> BuscarPedidos_By_IDCompradorAndEstadoPedido(UUID id_comprador, String estado_pedido);

    Set<Pedido> BuscarPedidosToEncargadoLaboratorio(UUID id_minera);

    Set<Pedido> ValidarPedidos(UUID id_comprador, UUID id_mineral);

    Optional<Pedido> BuscarPedido_By_IDPedido(UUID id_pedido);

    void GuardarPedido(Pedido pedido);

    void EliminarPedido(UUID id_pedido);
}
