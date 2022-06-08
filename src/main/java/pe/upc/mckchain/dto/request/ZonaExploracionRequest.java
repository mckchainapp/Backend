package pe.upc.mckchain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ZonaExploracionRequest {

    //Atributos
    private String nombreZonaExploracion;
    private String descripcionZonaExploracion;

    //Constructores
    public ZonaExploracionRequest() {
    }

    public ZonaExploracionRequest(String nombreZonaExploracion, String descripcionZonaExploracion) {
        this.nombreZonaExploracion = nombreZonaExploracion;
        this.descripcionZonaExploracion = descripcionZonaExploracion;
    }
}
