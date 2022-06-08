package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Imagen;

import java.util.Optional;
import java.util.UUID;

public interface IImagenService {

    Optional<Imagen> BuscarImagen_By_IDImagen(UUID id_imagen);

    Optional<Imagen> BuscarImagen_By_NombreImagen(String nombre_imagen);

    void GuardarImagen(Imagen imagen);
}
