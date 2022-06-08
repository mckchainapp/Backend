package pe.upc.mckchain.service;

import pe.upc.mckchain.model.UtilityToken;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface IUtilityTokenService {

    Optional<UtilityToken> BuscarUtilityToken_By_Token(String token_utilitytoken);

    Set<UtilityToken> BuscarUtilityToken_By_ExpiracionAndRazon(LocalDateTime expiracion_utilitytoken,
                                                               String razon_utilitytoken);

    Set<UtilityToken> BuscarUtilityToken_By_IDUsuarioAndRazonUtilityToken(UUID id_usuario, String razon_utilitytoken);

    void GuardarUtilityToken(UtilityToken utilitytoken);

    void EliminarUtilityToken(UUID id_utilitytoken);
}
