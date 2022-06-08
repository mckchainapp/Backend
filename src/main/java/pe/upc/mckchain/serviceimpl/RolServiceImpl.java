package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.enums.RolNombre;
import pe.upc.mckchain.model.Rol;
import pe.upc.mckchain.repository.IRolDAO;
import pe.upc.mckchain.service.IRolService;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@Transactional
public class RolServiceImpl implements IRolService {

    final
    IRolDAO data;

    public RolServiceImpl(IRolDAO data) {
        this.data = data;
    }

    @Override
    public Optional<Rol> BuscarRol_Nombre(RolNombre rol) {
        return data.findByNombreRol(rol);
    }

    @Override
    public void GuardarRol(Rol rol) {
        data.save(rol);
    }
}
