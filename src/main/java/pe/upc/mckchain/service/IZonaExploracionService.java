package pe.upc.mckchain.service;

import pe.upc.mckchain.model.ZonaExploracion;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IZonaExploracionService {

    Set<ZonaExploracion> BuscarZonasExploracion_By_IDMineraAndEstadoZonaExploracion(UUID id_minera,
                                                                                    String estado_zonaexploracion);

    Set<ZonaExploracion> ValidarZonasExploracion(UUID id_minera, String nombre_zonaexploracion);

    Optional<ZonaExploracion> BuscarZonaExploracion_By_IDZonaExploracion(UUID id_zonaexploracion);

    void GuardarZonaExploracion(ZonaExploracion zonaexploracion);

    void EliminarZonaExploracion(UUID id_zonaexploracion);
}
