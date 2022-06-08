package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Pedido;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IPedidoDAO extends JpaRepository<Pedido, UUID> {

    @Query("SELECT p FROM Pedido p " +
            "JOIN Usuario c ON p.compradorPedido.idUsuario = c.idUsuario " +
            "WHERE c.idUsuario = ?1 " +
            "AND p.estadoPedido LIKE ?2 " +
            "AND p.mineralPedido IS NOT NULL")
    Set<Pedido> findPedidosByIdCompradorAndEstadoPedido(UUID id_comprador, String estado_pedido);

    @Query("SELECT p FROM Pedido p " +
            "JOIN Usuario c ON p.compradorPedido.idUsuario = c.idUsuario " +
            "JOIN Mineral m ON m.pedidoMineral.idPedido = p.idPedido " +
            "WHERE c.idUsuario = ?1 " +
            "AND m.idMineral = ?2")
    Set<Pedido> findPedidosToValidate(UUID id_comprador, UUID id_mineral);

    @Query("SELECT p FROM Pedido p " +
            "JOIN Usuario mn ON p.mineralPedido.zonaexploracionMineral.mineraZonaExploracion.idUsuario = mn.idUsuario " +
            "JOIN Mineral m ON m.pedidoMineral.idPedido = p.idPedido " +
            "WHERE mn.idUsuario = ?1 " +
            "AND m.faseMineral LIKE 'PARA ESTUDIO' " +
            "AND p.estadoPedido LIKE 'PROCESANDO'")
    Set<Pedido> findPedidosToEncargadoLaboratorio(UUID id_minera);
}
