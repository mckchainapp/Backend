package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Imagen;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface IImagenDAO extends JpaRepository<Imagen, UUID> {

    Optional<Imagen> findByNombreImagen(String nombre_imagen);
}
