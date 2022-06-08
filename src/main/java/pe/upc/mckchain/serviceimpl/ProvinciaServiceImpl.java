package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Provincia;
import pe.upc.mckchain.repository.IProvinciaDAO;
import pe.upc.mckchain.service.IProvinciaService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class ProvinciaServiceImpl implements IProvinciaService {

    final
    IProvinciaDAO data;

    public ProvinciaServiceImpl(IProvinciaDAO data) {
        this.data = data;
    }

    @Override
    public Set<Provincia> MostrarProvincias() {
        return data.findAllProvincias();
    }

    @Override
    public Set<Provincia> BuscarProvincias_By_IDDepartamento(Long id_departamento) {
        return data.findProvinciasByIdDepartamento(id_departamento);
    }

    @Override
    public Optional<Provincia> BuscarProvincia_By_IDDistrito(Long id_distrito) {
        return data.findProvinciaByIdDistrito(id_distrito);
    }

    @Override
    public Optional<Provincia> BuscarProvincia_By_IDProvincia(Long id_provincia) {
        return data.findById(id_provincia);
    }

    @Override
    public Optional<Provincia> BuscarProvincia_ByIDDepartamentoAndNombreProvincia(Long id_departamento,
                                                                                  String nombre_provincia) {
        return data.findProvinciaByIdDepartamentoAndNombreProvincia(id_departamento, nombre_provincia);
    }
}
