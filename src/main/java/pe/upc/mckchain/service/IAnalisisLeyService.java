package pe.upc.mckchain.service;

import pe.upc.mckchain.model.AnalisisLey;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IAnalisisLeyService {

    Set<AnalisisLey> ValidarAnalisisLey(UUID id_encargadolaboratorio, UUID id_pedido);

    Set<AnalisisLey> BuscarAnalisisLey_By_IDMineraAndEstadoAnalisisLey(UUID id_minera, String estado_analisisley);

    Optional<AnalisisLey> BuscarAnalisisLey_By_IDAnalisisLey(UUID id_analisisley);

    void GuardarAnalisisLey(AnalisisLey analisisley);

    void EliminarAnalisisLey(UUID id_analisisley);
}
