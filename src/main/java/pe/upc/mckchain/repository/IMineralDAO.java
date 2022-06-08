package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Mineral;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IMineralDAO extends JpaRepository<Mineral, UUID> {

    @Query("SELECT m FROM Mineral m " +
            "JOIN ZonaExploracion ze ON m.zonaexploracionMineral.idZonaExploracion = ze.idZonaExploracion " +
            "JOIN Usuario um ON ze.mineraZonaExploracion.idUsuario = um.idUsuario " +
            "WHERE um.idUsuario = ?1 " +
            "AND m.estadoMineral LIKE ?2")
    Set<Mineral> findMineralesByIdMineraAndEstadoMineral(UUID id_minera, String estado_mineral);

    @Query("SELECT m FROM Mineral m " +
            "JOIN ZonaExploracion ze ON m.zonaexploracionMineral.idZonaExploracion = ze.idZonaExploracion " +
            "WHERE ze.idZonaExploracion = ?1 " +
            "AND m.nombreMineral LIKE ?2")
    Set<Mineral> findMineralesToValidate(UUID id_zonaexploracion, String nombre_mineral);

    @Query("SELECT m FROM Mineral m " +
            "WHERE m.estadoMineral LIKE 'HABILITADO' " +
            "AND m.habilitacionpedidoMineral = TRUE")
    Set<Mineral> findMineralesHabilitadosToPedido();

    @Query("SELECT m FROM Mineral m " +
            "JOIN Pedido p ON m.pedidoMineral.idPedido = p.idPedido " +
            "WHERE p.idPedido = ?1")
    Set<Mineral> findMineralesByIdPedido(UUID id_pedido);
}