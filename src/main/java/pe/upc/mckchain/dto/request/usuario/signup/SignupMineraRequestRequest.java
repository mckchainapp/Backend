package pe.upc.mckchain.dto.request.usuario.signup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupMineraRequestRequest {

    //Atributos
    private String codigounicoMinera;
    private String rucMinera;
    private String emailMinera;

    //Constructores
    public SignupMineraRequestRequest() {
    }

    public SignupMineraRequestRequest(String codigounicoMinera, String rucMinera, String emailMinera) {
        this.codigounicoMinera = codigounicoMinera;
        this.rucMinera = rucMinera;
        this.emailMinera = emailMinera;
    }
}
