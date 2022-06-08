package pe.upc.mckchain.service;

import pe.upc.mckchain.enums.RolNombre;
import pe.upc.mckchain.model.Rol;

import java.util.Optional;

public interface IRolService {

    Optional<Rol> BuscarRol_Nombre(RolNombre rol);

    void GuardarRol(Rol rol);
}
