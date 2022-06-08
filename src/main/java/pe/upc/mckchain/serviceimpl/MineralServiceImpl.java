package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Mineral;
import pe.upc.mckchain.repository.IMineralDAO;
import pe.upc.mckchain.service.IMineralService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class MineralServiceImpl implements IMineralService {

    final
    IMineralDAO data;

    public MineralServiceImpl(IMineralDAO data) {
        this.data = data;
    }

    @Override
    public Set<Mineral> BuscarMinerales_By_IDMineraAndEstadoMineral(UUID id_minera, String estado_mineral) {
        return data.findMineralesByIdMineraAndEstadoMineral(id_minera, estado_mineral);
    }

    @Override
    public Set<Mineral> ValidarMinerales(UUID id_zonaexploracion, String nombre_mineral) {
        return data.findMineralesToValidate(id_zonaexploracion, nombre_mineral);
    }

    @Override
    public Set<Mineral> MostrarMineralesToPedido() {
        return data.findMineralesHabilitadosToPedido();
    }

    @Override
    public Optional<Mineral> BuscarMineral_By_IDMineral(UUID id_mineral) {
        return data.findById(id_mineral);
    }

    @Override
    public void GuardarMineral(Mineral mineral) {
        data.save(mineral);
    }

    @Override
    public void EliminarMineral(UUID id_mineral) {
        data.deleteById(id_mineral);
    }
}
