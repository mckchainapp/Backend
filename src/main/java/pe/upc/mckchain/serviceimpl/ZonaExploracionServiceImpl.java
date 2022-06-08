package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.ZonaExploracion;
import pe.upc.mckchain.repository.IZonaExploracionDAO;
import pe.upc.mckchain.service.IZonaExploracionService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class ZonaExploracionServiceImpl implements IZonaExploracionService {

    final
    IZonaExploracionDAO data;

    public ZonaExploracionServiceImpl(IZonaExploracionDAO data) {
        this.data = data;
    }

    @Override
    public Set<ZonaExploracion> BuscarZonasExploracion_By_IDMineraAndEstadoZonaExploracion(UUID id_minera,
                                                                                           String estado_zonaexploracion) {
        return data.findZonasExploracionByIdMineraAndEstadoZonaExploracion(id_minera, estado_zonaexploracion);
    }

    @Override
    public Set<ZonaExploracion> ValidarZonasExploracion(UUID id_minera, String nombre_zonaexploracion) {
        return data.findZonasExploracionToValidate(id_minera, nombre_zonaexploracion);
    }

    @Override
    public Optional<ZonaExploracion> BuscarZonaExploracion_By_IDZonaExploracion(UUID id_zonaexploracion) {
        return data.findById(id_zonaexploracion);
    }

    @Override
    public void GuardarZonaExploracion(ZonaExploracion zonaexploracion) {
        data.save(zonaexploracion);
    }

    @Override
    public void EliminarZonaExploracion(UUID id_zonaexploracion) {
        data.deleteById(id_zonaexploracion);
    }
}
