package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.AnalisisLey;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IAnalisisLeyDAO extends JpaRepository<AnalisisLey, UUID> {

    @Query("SELECT al FROM AnalisisLey al " +
            "JOIN Usuario el ON al.encargadolaboratorioAnalisisLey.idUsuario = el.idUsuario " +
            "JOIN Pedido p ON al.pedidoAnalisisLey.idPedido = p.idPedido " +
            "WHERE el.idUsuario = ?1 " +
            "AND p.idPedido = ?2")
    Set<AnalisisLey> findAnalisisLeyToValidate(UUID id_encargadolaboratorio, UUID id_pedido);

    @Query("SELECT al FROM AnalisisLey al " +
            "JOIN Usuario el ON al.encargadolaboratorioAnalisisLey.idUsuario = el.idUsuario " +
            "WHERE el.mineraUsuario.idUsuario = ?1 " +
            "AND al.estadoAnalisisLey LIKE ?2")
    Set<AnalisisLey> findAnalisisLeyByIdMineraAndEstadoAnalisisLey(UUID id_minera, String estado_analisisley);
}
