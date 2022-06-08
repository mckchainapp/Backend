package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Provincia;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IProvinciaDAO extends JpaRepository<Provincia, Long> {

    @Query("SELECT p FROM Provincia p")
    Set<Provincia> findAllProvincias();

    @Query("SELECT p FROM Provincia p " +
            "JOIN Departamento d ON p.departamentoProvincia.idDepartamento = d.idDepartamento " +
            "WHERE d.idDepartamento = ?1")
    Set<Provincia> findProvinciasByIdDepartamento(Long id_departamento);

    @Query("SELECT p FROM Provincia p " +
            "JOIN Distrito d ON d.provinciaDistrito.idProvincia = p.idProvincia " +
            "WHERE d.idDistrito = ?1")
    Optional<Provincia> findProvinciaByIdDistrito(Long id_distrito);

    @Query("SELECT p FROM Provincia p " +
            "JOIN Departamento d ON p.departamentoProvincia.idDepartamento = d.idDepartamento " +
            "WHERE d.idDepartamento = ?1 " +
            "AND p.nombreProvincia LIKE ?2")
    Optional<Provincia> findProvinciaByIdDepartamentoAndNombreProvincia(Long id_departamento, String nombre_provincia);
}