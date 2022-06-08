package pe.upc.mckchain.controller.ubicacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.response.ubicacion.ProvinciaResponse;
import pe.upc.mckchain.service.IDepartamentoService;
import pe.upc.mckchain.service.IProvinciaService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/ubicacion")
@CrossOrigin
public class ProvinciaController {

    final
    IDepartamentoService departamentoService;

    final
    IProvinciaService provinciaService;

    public ProvinciaController(IDepartamentoService departamentoService, IProvinciaService provinciaService) {
        this.departamentoService = departamentoService;
        this.provinciaService = provinciaService;
    }

    @GetMapping("/show/provincias")
    public ResponseEntity<?> MostrarProvincias() {

        Set<ProvinciaResponse> lista_provincias = new HashSet<>();

        provinciaService.MostrarProvincias()
                .forEach(provincia -> lista_provincias.add(new ProvinciaResponse(
                        provincia.getIdProvincia(),
                        provincia.getNombreProvincia()
                )));

        return new ResponseEntity<>(lista_provincias,
                HttpStatus.OK);
    }

    @GetMapping("/find/provincias/from_departamento/{id_departamento}")
    public ResponseEntity<?> BuscarProvinciasPorDepartamento(@PathVariable("id_departamento") Long id_departamento) {

        Set<ProvinciaResponse> lista_provincias = new HashSet<>();

        provinciaService.BuscarProvincias_By_IDDepartamento(id_departamento)
                .forEach(provincia -> lista_provincias.add(new ProvinciaResponse(
                        provincia.getIdProvincia(),
                        provincia.getNombreProvincia()
                )));

        return new ResponseEntity<>(lista_provincias,
                HttpStatus.OK);
    }
}
