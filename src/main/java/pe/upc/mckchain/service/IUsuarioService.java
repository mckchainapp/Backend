package pe.upc.mckchain.service;

import pe.upc.mckchain.model.Usuario;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IUsuarioService {

    Set<Usuario> ValidarSolicitudRegistroMinera(UUID id_mckchain, String email_minera);

    Set<Usuario> ValidarSolicitudRegistroUsuario(UUID id_minera, String email_usuario);

    Set<Usuario> ValidarRegistroComprador(UUID id_mckchain, String ruc_comprador);

    Set<Usuario> ValidarProcesoRegistroMinera(UUID id_mckchain, String email_minera, String username_minera);

    Set<Usuario> ValidarProcesoRegistroUsuario(UUID id_minera, String email_usuario, String username_usuario);

    Set<Usuario> BuscarUsuarios_By_IDMinera(UUID id_minerauser);

    Optional<Usuario> BuscarUsuario_By_IDUsuario(UUID id_usuario);

    Optional<Usuario> BuscarUsuario_By_EmailUsuario(String email_usuario);

    Optional<Usuario> BuscarUsuario_By_UsernameOrEmail(String username_or_email);

    Optional<Usuario> BuscarUsuarioMinera_By_IDMineraData(UUID id_mineradata);

    Optional<Usuario> BuscarUsuario_By_IDUtilityToken(UUID id_utilitytoken);

    void GuardarUsuario(Usuario usuario);

    void EliminarUsuario(UUID id_usuario);
}
