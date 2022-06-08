package pe.upc.mckchain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReinfoResponse {

    //Atributos
    private String codigounicoMinera;
    private String estadoactividadMinera;
    private String rucMinera;
    private String nombreMinera;
    private String departamentoMinera;
    private String provinciaMinera;
    private String distritoMinera;

    //Constructores
    public ReinfoResponse() {
    }

    public ReinfoResponse(String codigounicoMinera, String estadoactividadMinera, String rucMinera, String nombreMinera,
                          String departamentoMinera, String provinciaMinera, String distritoMinera) {
        this.codigounicoMinera = codigounicoMinera;
        this.estadoactividadMinera = estadoactividadMinera;
        this.rucMinera = rucMinera;
        this.nombreMinera = nombreMinera;
        this.departamentoMinera = departamentoMinera;
        this.provinciaMinera = provinciaMinera;
        this.distritoMinera = distritoMinera;
    }
}
