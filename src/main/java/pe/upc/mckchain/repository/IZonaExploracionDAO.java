package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.ZonaExploracion;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IZonaExploracionDAO extends JpaRepository<ZonaExploracion, UUID> {

    @Query("SELECT ze FROM ZonaExploracion ze " +
            "JOIN Usuario m ON ze.mineraZonaExploracion.idUsuario = m.idUsuario " +
            "WHERE m.idUsuario = ?1 " +
            "AND ze.estadoZonaExploracion LIKE ?2")
    Set<ZonaExploracion> findZonasExploracionByIdMineraAndEstadoZonaExploracion(UUID id_minera, String
            estado_zonaexploracion);

    @Query("SELECT ze FROM ZonaExploracion ze " +
            "JOIN Usuario m ON ze.mineraZonaExploracion.idUsuario = m.idUsuario " +
            "WHERE m.idUsuario = ?1 " +
            "AND ze.nombreZonaExploracion LIKE ?2")
    Set<ZonaExploracion> findZonasExploracionToValidate(UUID id_minera, String nombre_zonaexploracion);
}
