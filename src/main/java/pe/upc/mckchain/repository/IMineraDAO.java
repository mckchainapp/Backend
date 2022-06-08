package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Minera;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IMineraDAO extends JpaRepository<Minera, UUID> {

    @Query("SELECT m FROM Minera m")
    Set<Minera> findAllMineras();

    @Query("SELECT m FROM Minera m " +
            "WHERE m.estadoactividadMinera LIKE ?1")
    Set<Minera> findAllMinerasByEstadoActividad(String estadoactividad_minera);

    @Query("SELECT m FROM Minera m " +
            "WHERE m.estadosolicitudMinera LIKE ?1")
    Set<Minera> findMinerasByEstadoSolicitud(String estadosolicitud_minera);

    @Query("SELECT m FROM Minera m " +
            "WHERE m.codigounicoMinera LIKE ?1 " +
            "AND m.rucMinera LIKE ?2 " +
            "AND m.estadoactividadMinera LIKE 'VIGENTE'")
    Optional<Minera> findMineraByCodigoUnicoMineraAndRucMinera(String codigounico_minera, String ruc_minera);

    @Query("SELECT m FROM Minera m " +
            "JOIN Usuario u ON m.mineraData.idUsuario = u.idUsuario " +
            "WHERE u.idUsuario = ?1")
    Optional<Minera> findMineraByIdUsuarioMinera(UUID id_usuariominera);
}
