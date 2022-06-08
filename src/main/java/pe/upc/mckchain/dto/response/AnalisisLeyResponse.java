package pe.upc.mckchain.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AnalisisLeyResponse {

    //Atributos
    private UUID idAnalisisLey;
    private String fecharegistroAnalisisLey;
    private String mineralAnalisisLey;
    private UUID idpedidoAnalisisLey;
    private String pesoAnalisisLey;
    private String distribuciongranulometricaAnalisisLey;
    private String humedadAnalisisLey;
    private String densidadpulpaAnalisisLey;
    private String caudalAnalisisLey;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreComprador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rucComprador;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreEncargadoLaboratorio;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String nombreMinera;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String rucMinera;

    //Constructores
    public AnalisisLeyResponse() {
    }

    //--Perspectiva Encargado Laboratorio
    public AnalisisLeyResponse(UUID idAnalisisLey, String fecharegistroAnalisisLey, String mineralAnalisisLey,
                               UUID idpedidoAnalisisLey, String pesoAnalisisLey,
                               String distribuciongranulometricaAnalisisLey, String humedadAnalisisLey,
                               String densidadpulpaAnalisisLey, String caudalAnalisisLey) {
        this.idAnalisisLey = idAnalisisLey;
        this.fecharegistroAnalisisLey = fecharegistroAnalisisLey;
        this.mineralAnalisisLey = mineralAnalisisLey;
        this.idpedidoAnalisisLey = idpedidoAnalisisLey;
        this.pesoAnalisisLey = pesoAnalisisLey;
        this.distribuciongranulometricaAnalisisLey = distribuciongranulometricaAnalisisLey;
        this.humedadAnalisisLey = humedadAnalisisLey;
        this.densidadpulpaAnalisisLey = densidadpulpaAnalisisLey;
        this.caudalAnalisisLey = caudalAnalisisLey;
    }

    //--Perspectiva Comprador
    public AnalisisLeyResponse(UUID idAnalisisLey, String fecharegistroAnalisisLey, String mineralAnalisisLey,
                               UUID idpedidoAnalisisLey, String pesoAnalisisLey,
                               String distribuciongranulometricaAnalisisLey, String humedadAnalisisLey,
                               String densidadpulpaAnalisisLey, String caudalAnalisisLey, String nombreComprador,
                               String rucComprador, String nombreEncargadoLaboratorio, String nombreMinera,
                               String rucMinera) {
        this.idAnalisisLey = idAnalisisLey;
        this.fecharegistroAnalisisLey = fecharegistroAnalisisLey;
        this.mineralAnalisisLey = mineralAnalisisLey;
        this.idpedidoAnalisisLey = idpedidoAnalisisLey;
        this.pesoAnalisisLey = pesoAnalisisLey;
        this.distribuciongranulometricaAnalisisLey = distribuciongranulometricaAnalisisLey;
        this.humedadAnalisisLey = humedadAnalisisLey;
        this.densidadpulpaAnalisisLey = densidadpulpaAnalisisLey;
        this.caudalAnalisisLey = caudalAnalisisLey;
        this.nombreComprador = nombreComprador;
        this.rucComprador = rucComprador;
        this.nombreEncargadoLaboratorio = nombreEncargadoLaboratorio;
        this.nombreMinera = nombreMinera;
        this.rucMinera = rucMinera;
    }
}
