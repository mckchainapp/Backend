package pe.upc.mckchain.controller.ubicacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.upc.mckchain.dto.response.ubicacion.DepartamentoResponse;
import pe.upc.mckchain.service.IDepartamentoService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/ubicacion")
@CrossOrigin
public class DepartamentoController {

    final
    IDepartamentoService departamentoService;

    public DepartamentoController(IDepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }


    @GetMapping("/show/departamentos")
    public ResponseEntity<?> MostrarDepartamentos() {

        Set<DepartamentoResponse> lista_departamentos = new HashSet<>();

        departamentoService.MostrarDepartamentos()
                .forEach(departamento -> lista_departamentos.add(new DepartamentoResponse(
                        departamento.getIdDepartamento(),
                        departamento.getNombreDepartamento()
                )));

        return new ResponseEntity<>(lista_departamentos,
                HttpStatus.OK);
    }
}
