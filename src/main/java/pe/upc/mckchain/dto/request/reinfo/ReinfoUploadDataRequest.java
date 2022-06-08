package pe.upc.mckchain.dto.request.reinfo;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ReinfoUploadDataRequest {

    //Atributos
    Set<ReinfoSingleDataRequest> reinfoData;

    //Constructores
    public ReinfoUploadDataRequest() {
    }

    public ReinfoUploadDataRequest(Set<ReinfoSingleDataRequest> reinfoData) {
        this.reinfoData = reinfoData;
    }
}
