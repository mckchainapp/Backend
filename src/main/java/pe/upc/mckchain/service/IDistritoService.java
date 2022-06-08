package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Distrito;

import java.util.Optional;
import java.util.Set;

public interface IDistritoService {

    Set<Distrito> MostrarDistritos();

    Set<Distrito> BuscarDistritos_By_IDProvincia(Long id_provincia);

    Optional<Distrito> BuscarDistrito_By_IDDistrito(Long id_distrito);

    Optional<Distrito> BuscarDistrito_By_IDProvinciaAndNombreDistrito(Long id_provincia, String nombre_distrito);
}
