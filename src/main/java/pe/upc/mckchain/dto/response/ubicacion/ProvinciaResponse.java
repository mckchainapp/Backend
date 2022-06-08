package pe.upc.mckchain.dto.response.ubicacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProvinciaResponse {

    //Atributos
    private Long idProvincia;
    private String nombreProvincia;

    //Constructores
    public ProvinciaResponse() {
    }

    //---Lista de Provincias
    public ProvinciaResponse(Long idProvincia, String nombreProvincia) {
        this.idProvincia = idProvincia;
        this.nombreProvincia = nombreProvincia;
    }
}
