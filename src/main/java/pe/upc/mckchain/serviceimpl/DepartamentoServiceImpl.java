package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Departamento;
import pe.upc.mckchain.repository.IDepartamentoDAO;
import pe.upc.mckchain.service.IDepartamentoService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DepartamentoServiceImpl implements IDepartamentoService {

    final
    IDepartamentoDAO data;

    public DepartamentoServiceImpl(IDepartamentoDAO data) {
        this.data = data;
    }

    @Override
    public Set<Departamento> MostrarDepartamentos() {
        return data.findAllDepartamentos();
    }

    @Override
    public Optional<Departamento> BuscarDepartamento_By_IDProvincia(Long id_provincia) {
        return data.findDepartamentoByIdProvincia(id_provincia);
    }

    @Override
    public Optional<Departamento> BuscarDepartamento_By_IDDepartamento(Long id_departamento) {
        return data.findById(id_departamento);
    }

    @Override
    public Optional<Departamento> BuscarDepartamento_By_NombreDepartamento(String nombre_departamento) {
        return data.findByNombreDepartamento(nombre_departamento);
    }
}
