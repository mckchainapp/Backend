package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.repository.IUsuarioDAO;
import pe.upc.mckchain.service.IUsuarioService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UsuarioServiceImpl implements IUsuarioService {

    final
    IUsuarioDAO data;

    public UsuarioServiceImpl(IUsuarioDAO data) {
        this.data = data;
    }

    @Override
    public Set<Usuario> ValidarSolicitudRegistroMinera(UUID id_mckchain, String email_minera) {
        return data.findMinerasToValidateSignupRequest(id_mckchain, email_minera);
    }

    @Override
    public Set<Usuario> ValidarSolicitudRegistroUsuario(UUID id_minera, String email_usuario) {
        return data.findUsuariosToValidateSignupRequest(id_minera, email_usuario);
    }

    @Override
    public Set<Usuario> ValidarRegistroComprador(UUID id_mckchain, String ruc_comprador) {
        return data.findCompradoresToValidateSignup(id_mckchain, ruc_comprador);
    }

    @Override
    public Set<Usuario> ValidarProcesoRegistroMinera(UUID id_mckchain, String email_minera, String username_minera) {
        return data.findMinerasToValidateSignupProcess(id_mckchain, email_minera, username_minera);
    }

    @Override
    public Set<Usuario> ValidarProcesoRegistroUsuario(UUID id_minera, String email_usuario, String username_usuario) {
        return data.findUsuariosToValidateSignupProcess(id_minera, email_usuario, username_usuario);
    }

    @Override
    public Set<Usuario> BuscarUsuarios_By_IDMinera(UUID id_minerauser) {
        return data.findUsuariosByIdMinera(id_minerauser);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_By_IDUsuario(UUID id_usuario) {
        return data.findById(id_usuario);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_By_EmailUsuario(String email_usuario) {
        return data.findByEmailUsuario(email_usuario);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_By_UsernameOrEmail(String username_or_email) {
        return data.findByUsernameUsuarioOrEmailUsuario(username_or_email);
    }

    @Override
    public Optional<Usuario> BuscarUsuarioMinera_By_IDMineraData(UUID id_mineradata) {
        return data.findUsuarioMineraByIdMineraData(id_mineradata);
    }

    @Override
    public Optional<Usuario> BuscarUsuario_By_IDUtilityToken(UUID id_utilitytoken) {
        return data.findByIdUtilityToken(id_utilitytoken);
    }

    @Override
    public void GuardarUsuario(Usuario usuario) {
        data.save(usuario);
    }

    @Override
    public void EliminarUsuario(UUID id_usuario) {
        data.deleteById(id_usuario);
    }
}
