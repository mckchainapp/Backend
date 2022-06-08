package pe.upc.mckchain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pe.upc.mckchain.model.Usuario;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface IUsuarioDAO extends JpaRepository<Usuario, UUID> {

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mckchainMinera.idUsuario = ?1 " +
            "AND u.emailUsuario LIKE ?2")
    Set<Usuario> findMinerasToValidateSignupRequest(UUID id_mckchain, String email_minera);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mineraUsuario.idUsuario = ?1 " +
            "AND u.emailUsuario LIKE ?2")
    Set<Usuario> findUsuariosToValidateSignupRequest(UUID id_minera, String email_usuario);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mckchainComprador.idUsuario = ?1 " +
            "AND u.rucUsuario LIKE ?2")
    Set<Usuario> findCompradoresToValidateSignup(UUID id_mckchain, String ruc_comprador);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mckchainMinera.idUsuario = ?1 " +
            "AND u.emailUsuario LIKE ?2 " +
            "AND u.usernameUsuario LIKE ?3")
    Set<Usuario> findMinerasToValidateSignupProcess(UUID id_mckchain, String email_minera, String username_minera);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mineraUsuario.idUsuario = ?1 " +
            "AND u.emailUsuario LIKE ?2 " +
            "AND u.usernameUsuario LIKE ?3")
    Set<Usuario> findUsuariosToValidateSignupProcess(UUID id_minera, String email_usuario, String username_usuario);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.mineraUsuario.idUsuario = ?1")
    Set<Usuario> findUsuariosByIdMinera(UUID id_minerauser);

    Optional<Usuario> findByUsernameUsuario(String username_usuario);

    Optional<Usuario> findByEmailUsuario(String email_usuario);

    @Query("SELECT u FROM Usuario u " +
            "WHERE u.usernameUsuario LIKE ?1 " +
            "OR u.emailUsuario LIKE ?1")
    Optional<Usuario> findByUsernameUsuarioOrEmailUsuario(String username_or_email);

    @Query("SELECT u FROM Usuario u " +
            "JOIN Minera m ON u.dataMinera.idMinera = m.idMinera " +
            "WHERE m.idMinera = ?1")
    Optional<Usuario> findUsuarioMineraByIdMineraData(UUID id_mineradata);

    @Query("SELECT u FROM Usuario u " +
            "JOIN UtilityToken ut ON u.idUsuario = ut.usuarioUtilityToken.idUsuario " +
            "WHERE ut.idUtilityToken = ?1")
    Optional<Usuario> findByIdUtilityToken(UUID id_utilitytoken);
}
