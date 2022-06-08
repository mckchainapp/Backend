package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.UtilityToken;
import pe.upc.mckchain.repository.IUtilityTokenDAO;
import pe.upc.mckchain.service.IUtilityTokenService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@Transactional
public class UtilityTokenServiceImpl implements IUtilityTokenService {

    final
    IUtilityTokenDAO data;

    public UtilityTokenServiceImpl(IUtilityTokenDAO data) {
        this.data = data;
    }

    @Override
    public Optional<UtilityToken> BuscarUtilityToken_By_Token(String token_utilitytoken) {
        return data.findByTokenUtilityToken(token_utilitytoken);
    }

    @Override
    public Set<UtilityToken> BuscarUtilityToken_By_ExpiracionAndRazon(LocalDateTime expiracion_utilitytoken,
                                                                      String razon_utilitytoken) {
        return data.findUtilityTokenByExpiracionAndRazon(expiracion_utilitytoken, razon_utilitytoken);
    }

    @Override
    public Set<UtilityToken> BuscarUtilityToken_By_IDUsuarioAndRazonUtilityToken(UUID id_usuario,
                                                                                 String razon_utilitytoken) {
        return data.findUtilityTokensByIdUsuarioAndRazon(id_usuario, razon_utilitytoken);
    }

    @Override
    public void GuardarUtilityToken(UtilityToken utilitytoken) {
        data.save(utilitytoken);
    }

    @Override
    public void EliminarUtilityToken(UUID id_utilitytoken) {
        data.deleteById(id_utilitytoken);
    }
}
