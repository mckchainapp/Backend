package pe.upc.mckchain.controller.util.util_code;

import pe.upc.mckchain.model.Rol;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.IRolService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IUtilityTokenService;

import java.util.Set;

public class Code_SignupUserMethods {

    public static void RollbackSignupFromOtherEntities(Usuario usuario, Rol rol, UtilityToken utilitytoken,
                                                       IUsuarioService usuarioService, IRolService rolService,
                                                       IUtilityTokenService utilityTokenService) {

        usuario.setPasswordUsuario(null);
        usuario.setFecharegistroUsuario(null);
        usuario.setEstadoUsuario("PENDIENTE");

        usuarioService.GuardarUsuario(usuario);

        //--Rollback Rol
        if (usuario.getRolesUsuario() != null) {
            Set<Usuario> list_usuarios = rol.getUsuariosRoles();
            list_usuarios.remove(usuario);

            rolService.GuardarRol(rol);
        }

        //--Rollback UtilityToken
        if (usuario.getUtilitytokensUsuario() != null) {
            utilityTokenService.GuardarUtilityToken(utilitytoken);
        }
    }

    public static void RollbackSignup_Minera(Usuario minera, Rol rol, UtilityToken utilitytoken,
                                             IUsuarioService usuarioService, IRolService rolService,
                                             IUtilityTokenService utilityTokenService) {

        //---Rollback Minera
        minera.setUsernameUsuario(null);

        RollbackSignupFromOtherEntities(minera, rol, utilitytoken, usuarioService, rolService, utilityTokenService);
    }

    public static void RollbackSignup_Usuario(Usuario agricultor, Rol rol, UtilityToken utilitytoken,
                                              IUsuarioService usuarioService, IRolService rolService,
                                              IUtilityTokenService utilityTokenService) {
        //---Rollback Usuario
        agricultor.setUsernameUsuario(null);
        agricultor.setNombreUsuario(null);
        agricultor.setApellidoUsuario(null);

        RollbackSignupFromOtherEntities(agricultor, rol, utilitytoken, usuarioService, rolService, utilityTokenService);
    }
}
