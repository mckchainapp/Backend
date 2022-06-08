package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Departamento;

import java.util.Optional;
import java.util.Set;

public interface IDepartamentoService {

    Set<Departamento> MostrarDepartamentos();

    Optional<Departamento> BuscarDepartamento_By_IDProvincia(Long id_provincia);

    Optional<Departamento> BuscarDepartamento_By_IDDepartamento(Long id_departamento);

    Optional<Departamento> BuscarDepartamento_By_NombreDepartamento(String nombre_departamento);
}
