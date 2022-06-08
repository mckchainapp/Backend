package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Mineral;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IMineralService {

    Set<Mineral> BuscarMinerales_By_IDMineraAndEstadoMineral(UUID id_minera, String estado_mineral);

    Set<Mineral> ValidarMinerales(UUID id_zonaexploracion, String nombre_mineral);

    Set<Mineral> MostrarMineralesToPedido();

    Optional<Mineral> BuscarMineral_By_IDMineral(UUID id_mineral);

    void GuardarMineral(Mineral mineral);

    void EliminarMineral(UUID id_mineral);
}
