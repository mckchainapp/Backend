package pe.upc.mckchain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class SignupRequestMineraResponse {

    //Atributos
    private UUID idMinera;
    private String codigounicoMinera;
    private String rucMinera;
    private String nombreMinera;
    private String emailMinera;
    private LocalDateTime fechahorasolicitudMinera;
    private String fechasolicitudMinera;

    //Constructores
    public SignupRequestMineraResponse() {
    }

    public SignupRequestMineraResponse(UUID idMinera, String codigounicoMinera, String rucMinera, String nombreMinera,
                                       String emailMinera, LocalDateTime fechahorasolicitudMinera,
                                       String fechasolicitudMinera) {
        this.idMinera = idMinera;
        this.codigounicoMinera = codigounicoMinera;
        this.rucMinera = rucMinera;
        this.nombreMinera = nombreMinera;
        this.emailMinera = emailMinera;
        this.fechahorasolicitudMinera = fechahorasolicitudMinera;
        this.fechasolicitudMinera = fechasolicitudMinera;
    }
}
