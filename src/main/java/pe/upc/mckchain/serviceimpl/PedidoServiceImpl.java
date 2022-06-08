package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Pedido;
import pe.upc.mckchain.repository.IPedidoDAO;
import pe.upc.mckchain.service.IPedidoService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class PedidoServiceImpl implements IPedidoService {

    final
    IPedidoDAO data;

    public PedidoServiceImpl(IPedidoDAO data) {
        this.data = data;
    }

    @Override
    public Set<Pedido> BuscarPedidos_By_IDCompradorAndEstadoPedido(UUID id_comprador, String estado_pedido) {
        return data.findPedidosByIdCompradorAndEstadoPedido(id_comprador, estado_pedido);
    }

    @Override
    public Set<Pedido> BuscarPedidosToEncargadoLaboratorio(UUID id_minera) {
        return data.findPedidosToEncargadoLaboratorio(id_minera);
    }

    @Override
    public Set<Pedido> ValidarPedidos(UUID id_comprador, UUID id_mineral) {
        return data.findPedidosToValidate(id_comprador, id_mineral);
    }

    @Override
    public Optional<Pedido> BuscarPedido_By_IDPedido(UUID id_pedido) {
        return data.findById(id_pedido);
    }

    @Override
    public void GuardarPedido(Pedido pedido) {
        data.save(pedido);
    }

    @Override
    public void EliminarPedido(UUID id_pedido) {
        data.deleteById(id_pedido);
    }
}
