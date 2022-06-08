package pe.upc.mckchain.dto.response.ubicacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DistritoResponse {

    //Atributos
    private Long idDistrito;
    private String nombreDistrito;

    //Constructores
    public DistritoResponse() {
    }

    //---Lista de Distritos
    public DistritoResponse(Long idDistrito, String nombreDistrito) {
        this.idDistrito = idDistrito;
        this.nombreDistrito = nombreDistrito;
    }
}
