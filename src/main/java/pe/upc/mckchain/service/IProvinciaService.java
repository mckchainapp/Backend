package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Provincia;

import java.util.Optional;
import java.util.Set;

public interface IProvinciaService {

    Set<Provincia> MostrarProvincias();

    Set<Provincia> BuscarProvincias_By_IDDepartamento(Long id_departamento);

    Optional<Provincia> BuscarProvincia_By_IDDistrito(Long id_distrito);

    Optional<Provincia> BuscarProvincia_By_IDProvincia(Long id_provincia);

    Optional<Provincia> BuscarProvincia_ByIDDepartamentoAndNombreProvincia(Long id_departamento,
                                                                           String nombre_provincia);
}