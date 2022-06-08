package pe.upc.mckchain.dto.response.ubicacion;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DepartamentoResponse {

    //Atributos
    private Long idDepartamento;
    private String nombreDepartamento;

    //Constructores
    public DepartamentoResponse() {
    }

    //---Lista de Departamentos
    public DepartamentoResponse(Long idDepartamento, String nombreDepartamento) {
        this.idDepartamento = idDepartamento;
        this.nombreDepartamento = nombreDepartamento;
    }
}
