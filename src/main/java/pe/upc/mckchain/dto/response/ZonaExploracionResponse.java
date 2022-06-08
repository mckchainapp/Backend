package pe.upc.mckchain.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class ZonaExploracionResponse {

    //Atributos
    private UUID idZonaExploracion;
    private String nombreZonaExploracion;
    private String descripcionZonaExploracion;
    private String fecharegistroZonaExploracion;

    //Constructores
    public ZonaExploracionResponse() {
    }

    public ZonaExploracionResponse(UUID idZonaExploracion, String nombreZonaExploracion,
                                   String descripcionZonaExploracion, String fecharegistroZonaExploracion) {
        this.idZonaExploracion = idZonaExploracion;
        this.nombreZonaExploracion = nombreZonaExploracion;
        this.descripcionZonaExploracion = descripcionZonaExploracion;
        this.fecharegistroZonaExploracion = fecharegistroZonaExploracion;
    }
}
