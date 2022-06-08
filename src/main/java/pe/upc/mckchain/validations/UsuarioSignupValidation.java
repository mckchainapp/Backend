package pe.upc.mckchain.validations;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class UsuarioSignupValidation {

    //Atributos
    private UUID idAdminUser;
    private String paramUno;
    private String paramDos;

    //Constructores
    public UsuarioSignupValidation() {
    }

    //---Validación de Signup Send Request Minera
    //---Validación de Signup Comprador
    public UsuarioSignupValidation(UUID idAdminUser, String paramUno) {
        this.idAdminUser = idAdminUser;
        this.paramUno = paramUno;
    }

    //---Validación de Signup User
    public UsuarioSignupValidation(UUID idAdminUser, String paramUno, String paramDos) {
        this.idAdminUser = idAdminUser;
        this.paramUno = paramUno;
        this.paramDos = paramDos;
    }
}
