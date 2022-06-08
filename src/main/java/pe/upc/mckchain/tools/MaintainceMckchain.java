package pe.upc.mckchain.tools;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pe.upc.mckchain.model.Minera;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.service.IMineraService;
import pe.upc.mckchain.service.IUsuarioService;
import pe.upc.mckchain.service.IUtilityTokenService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@Transactional
public class MaintainceMckchain {

    final
    IUtilityTokenService utilityTokenService;

    final
    IUsuarioService usuarioService;

    final
    IMineraService mineraService;

    public MaintainceMckchain(IUtilityTokenService utilityTokenService, IUsuarioService usuarioService,
                              IMineraService mineraService) {
        this.utilityTokenService = utilityTokenService;
        this.usuarioService = usuarioService;
        this.mineraService = mineraService;
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarMineraSignupTimedOut() {
        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(
                fechahora_actual,
                "Signup Minera");

        EliminarUsuario_UtilityToken(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarOperarioSignupTimedOut() {
        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(
                fechahora_actual,
                "Signup Operario");

        EliminarUsuario_UtilityToken(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarEncargadoLaboratorioSignupTimedOut() {
        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(
                fechahora_actual,
                "Signup Encargado Laboratorio");

        EliminarUsuario_UtilityToken(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarCompradorSignupTimedOut() {
        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(
                fechahora_actual,
                "Signup Comprador");

        EliminarUsuario_UtilityToken(utilitytoken_list);
    }

    @Scheduled(fixedRate = 60000)
    public void ActualizarEstadoUsuarioMinera_UserInactivo() {
        Set<Minera> list_mineras = mineraService.BuscarMineras_By_EstadoActividadMinera("SUSPENDIDO");

        Set<Minera> list_mineras_with_userdata = new HashSet<>();

        for (Minera minera_with_data : list_mineras) {
            if (minera_with_data.getMineraData() != null) {
                list_mineras_with_userdata.add(minera_with_data);
            }
        }

        for (Minera minera : list_mineras_with_userdata) {
            Optional<Usuario> usuariominera_data =
                    usuarioService.BuscarUsuarioMinera_By_IDMineraData(minera.getIdMinera());

            if (usuariominera_data.isPresent()) {
                Usuario minera_user = usuariominera_data.get();

                minera_user.setEstadoUsuario("INACTIVO");

                usuarioService.GuardarUsuario(minera_user);

                Set<Usuario> list_usuarios_minera =
                        usuarioService.BuscarUsuarios_By_IDMinera(minera_user.getIdUsuario());

                for (Usuario usuario : list_usuarios_minera) {
                    usuario.setEstadoUsuario("INACTIVO");

                    usuarioService.GuardarUsuario(usuario);
                }
            } else {
                System.out.println("Ocurrió un error al buscar la información de REINFO de la Minera: " +
                        minera.getNombreMinera() + " con RUC: " + minera.getRucMinera());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void ActualizarEstadoUsuarioMinera_UserActivo() {
        Set<Minera> list_mineras = mineraService.BuscarMineras_By_EstadoActividadMinera("VIGENTE");

        Set<Minera> list_mineras_with_userdata = new HashSet<>();

        for (Minera minera_with_data : list_mineras) {
            if (minera_with_data.getMineraData() != null) {
                list_mineras_with_userdata.add(minera_with_data);
            }
        }

        for (Minera minera : list_mineras_with_userdata) {
            Optional<Usuario> usuariominera_data =
                    usuarioService.BuscarUsuarioMinera_By_IDMineraData(minera.getIdMinera());

            if (usuariominera_data.isPresent()) {
                Usuario minera_user = usuariominera_data.get();

                minera_user.setEstadoUsuario("ACTIVO");

                usuarioService.GuardarUsuario(minera_user);

                Set<Usuario> list_usuarios_minera =
                        usuarioService.BuscarUsuarios_By_IDMinera(minera_user.getIdUsuario());

                for (Usuario usuario : list_usuarios_minera) {
                    usuario.setEstadoUsuario("ACTIVO");

                    usuarioService.GuardarUsuario(usuario);
                }
            } else {
                System.out.println("Ocurrió un error al buscar la información de REINFO de la Minera: " +
                        minera.getNombreMinera() + " con RUC: " + minera.getRucMinera());
            }
        }
    }

    @Scheduled(fixedRate = 60000)
    public void EliminarRestorePasswordUtilityToken() {
        //Asignando Fecha de Registro Actual
        ZoneId zona_actual = ZoneId.of("America/Lima");
        LocalDateTime fechahora_actual = LocalDateTime.now().atZone(zona_actual).toLocalDateTime();

        Set<UtilityToken> utilitytoken_list = utilityTokenService.BuscarUtilityToken_By_ExpiracionAndRazon(
                fechahora_actual,
                "Restore Password");

        for (UtilityToken restoretoken : utilitytoken_list) {
            Optional<Usuario> usuario_data =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(restoretoken.getIdUtilityToken());

            if (usuario_data.isPresent()) {
                Usuario usuario = usuario_data.get();
                usuario.setEstadoUsuario("ACTIVO");

                usuarioService.GuardarUsuario(usuario);
            }

            utilityTokenService.EliminarUtilityToken(restoretoken.getIdUtilityToken());
        }
    }

    private void EliminarUsuario_UtilityToken(Set<UtilityToken> list_utilitytokens) {
        for (UtilityToken utilitytoken : list_utilitytokens) {
            Optional<Usuario> usuario_timedout =
                    usuarioService.BuscarUsuario_By_IDUtilityToken(utilitytoken.getIdUtilityToken());

            if (usuario_timedout.isPresent()) {
                Usuario usuario = usuario_timedout.get();

                usuarioService.EliminarUsuario(usuario.getIdUsuario());
            }
        }
    }
}


