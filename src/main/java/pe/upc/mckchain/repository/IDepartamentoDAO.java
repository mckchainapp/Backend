package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Departamento;

import java.util.Optional;
import java.util.Set;

@Repository
public interface IDepartamentoDAO extends JpaRepository<Departamento, Long> {

    @Query("SELECT d FROM Departamento d")
    Set<Departamento> findAllDepartamentos();

    @Query("SELECT d FROM Departamento d " +
            "JOIN Provincia p ON p.departamentoProvincia.idDepartamento = d.idDepartamento " +
            "WHERE p.idProvincia = ?1")
    Optional<Departamento> findDepartamentoByIdProvincia(Long id_provincia);

    Optional<Departamento> findByNombreDepartamento(String nombre_departamento);
}
