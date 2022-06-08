package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.AnalisisLey;
import pe.upc.mckchain.repository.IAnalisisLeyDAO;
import pe.upc.mckchain.service.IAnalisisLeyService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class AnalisisLeyServiceImpl implements IAnalisisLeyService {

    final
    IAnalisisLeyDAO data;

    public AnalisisLeyServiceImpl(IAnalisisLeyDAO data) {
        this.data = data;
    }

    @Override
    public Set<AnalisisLey> ValidarAnalisisLey(UUID id_encargadolaboratorio, UUID id_pedido) {
        return data.findAnalisisLeyToValidate(id_encargadolaboratorio, id_pedido);
    }

    @Override
    public Set<AnalisisLey> BuscarAnalisisLey_By_IDMineraAndEstadoAnalisisLey(UUID id_minera, String estado_analisisley) {
        return data.findAnalisisLeyByIdMineraAndEstadoAnalisisLey(id_minera, estado_analisisley);
    }

    @Override
    public Optional<AnalisisLey> BuscarAnalisisLey_By_IDAnalisisLey(UUID id_analisisley) {
        return data.findById(id_analisisley);
    }

    @Override
    public void GuardarAnalisisLey(AnalisisLey analisisley) {
        data.save(analisisley);
    }

    @Override
    public void EliminarAnalisisLey(UUID id_analisisley) {
        data.deleteById(id_analisisley);
    }
}
