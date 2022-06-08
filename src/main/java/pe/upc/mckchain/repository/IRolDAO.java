package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.enums.RolNombre;
import pe.upc.mckchain.model.Rol;

import java.util.Optional;

@Repository
public interface IRolDAO extends JpaRepository<Rol, Integer> {

    Optional<Rol> findByNombreRol(RolNombre rol);
}
