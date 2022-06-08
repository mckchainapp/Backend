package pe.upc.mckchain.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalisisLeyRequest {

    //Atributos
    private String pedido;
    private String pesoAnalisisLey;
    private String distribuciongranulometricaAnalisisLey;
    private String humedadAnalisisLey;
    private String densidadpulpaAnalisisLey;
    private String caudalAnalisisLey;

    //Constructores
    public AnalisisLeyRequest() {
    }

    public AnalisisLeyRequest(String pedido, String pesoAnalisisLey, String distribuciongranulometricaAnalisisLey,
                              String humedadAnalisisLey, String densidadpulpaAnalisisLey, String caudalAnalisisLey) {
        this.pedido = pedido;
        this.pesoAnalisisLey = pesoAnalisisLey;
        this.distribuciongranulometricaAnalisisLey = distribuciongranulometricaAnalisisLey;
        this.humedadAnalisisLey = humedadAnalisisLey;
        this.densidadpulpaAnalisisLey = densidadpulpaAnalisisLey;
        this.caudalAnalisisLey = caudalAnalisisLey;
    }
}
