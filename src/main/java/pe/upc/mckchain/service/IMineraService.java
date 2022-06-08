package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Minera;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IMineraService {

    Set<Minera> MostrarMineras();

    Set<Minera> BuscarMineras_By_EstadoActividadMinera(String estadoactividad_minera);

    Set<Minera> BuscarMineras_By_EstadoSolicitudMinera(String estadosolicitud_minera);

    Optional<Minera> BuscarMinera_By_IDMinera(UUID id_minera);

    Optional<Minera> BuscarMinera_By_CodigoUnicoMineraAndRucMinera(String codigounico_minera, String ruc_minera);

    Optional<Minera> BuscarMinera_By_IDUsuarioMinera(UUID id_usuariominera);

    void GuardarMinera(Minera minera);

    void EliminarMinera(UUID id_minera);
}
