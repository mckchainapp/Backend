package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Minera;
import pe.upc.mckchain.repository.IMineraDAO;
import pe.upc.mckchain.service.IMineraService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class MineraServiceImpl implements IMineraService {

    final
    IMineraDAO data;

    public MineraServiceImpl(IMineraDAO data) {
        this.data = data;
    }

    @Override
    public Set<Minera> MostrarMineras() {
        return data.findAllMineras();
    }

    @Override
    public Set<Minera> BuscarMineras_By_EstadoActividadMinera(String estadoactividad_minera) {
        return data.findAllMinerasByEstadoActividad(estadoactividad_minera);
    }

    @Override
    public Set<Minera> BuscarMineras_By_EstadoSolicitudMinera(String estadosolicitud_minera) {
        return data.findMinerasByEstadoSolicitud(estadosolicitud_minera);
    }

    @Override
    public Optional<Minera> BuscarMinera_By_IDMinera(UUID id_minera) {
        return data.findById(id_minera);
    }

    @Override
    public Optional<Minera> BuscarMinera_By_CodigoUnicoMineraAndRucMinera(String codigounico_minera, String ruc_minera) {
        return data.findMineraByCodigoUnicoMineraAndRucMinera(codigounico_minera, ruc_minera);
    }

    @Override
    public Optional<Minera> BuscarMinera_By_IDUsuarioMinera(UUID id_usuariominera) {
        return data.findMineraByIdUsuarioMinera(id_usuariominera);
    }

    @Override
    public void GuardarMinera(Minera minera) {
        data.save(minera);
    }

    @Override
    public void EliminarMinera(UUID id_minera) {
        data.deleteById(id_minera);
    }
}
