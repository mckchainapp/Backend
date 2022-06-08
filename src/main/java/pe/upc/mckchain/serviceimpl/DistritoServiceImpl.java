package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Distrito;
import pe.upc.mckchain.repository.IDistritoDAO;
import pe.upc.mckchain.service.IDistritoService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class DistritoServiceImpl implements IDistritoService {

    final
    IDistritoDAO data;

    public DistritoServiceImpl(IDistritoDAO data) {
        this.data = data;
    }

    @Override
    public Set<Distrito> MostrarDistritos() {
        return data.findAllDistritos();
    }

    @Override
    public Set<Distrito> BuscarDistritos_By_IDProvincia(Long id_provincia) {
        return data.findDistritosByIdProvincia(id_provincia);
    }

    @Override
    public Optional<Distrito> BuscarDistrito_By_IDDistrito(Long id_distrito) {
        return data.findById(id_distrito);
    }

    @Override
    public Optional<Distrito> BuscarDistrito_By_IDProvinciaAndNombreDistrito(Long id_provincia, String nombre_distrito) {
        return data.findDistritoByIdProvinciaAndNombreDistrito(id_provincia, nombre_distrito);
    }
}
