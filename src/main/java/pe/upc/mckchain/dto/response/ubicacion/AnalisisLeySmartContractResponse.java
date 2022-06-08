package pe.upc.mckchain.dto.response.ubicacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalisisLeySmartContractResponse {

    //Atributos
    private String tramaAnalisisLeySmartContract;

    //Constructores
    public AnalisisLeySmartContractResponse() {
    }

    public AnalisisLeySmartContractResponse(String tramaAnalisisLeySmartContract) {
        this.tramaAnalisisLeySmartContract = tramaAnalisisLeySmartContract;
    }
}
