package pe.upc.mckchain.dto.response.general;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImagenResponse {

    //Atributos
    private String nombreImagen;
    private String urlImagen;

    //Constructores
    public ImagenResponse() {
    }

    public ImagenResponse(String nombreImagen, String urlImagen) {
        this.nombreImagen = nombreImagen;
        this.urlImagen = urlImagen;
    }
}
