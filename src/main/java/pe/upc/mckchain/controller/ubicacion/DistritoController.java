package pe.upc.mckchain.controller.ubicacion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.response.ubicacion.DistritoResponse;
import pe.upc.mckchain.service.IDistritoService;
import pe.upc.mckchain.service.IProvinciaService;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/ubicacion")
@CrossOrigin
public class DistritoController {

    final
    IProvinciaService provinciaService;

    final
    IDistritoService distritoService;

    public DistritoController(IProvinciaService provinciaService, IDistritoService distritoService) {
        this.provinciaService = provinciaService;
        this.distritoService = distritoService;
    }

    @GetMapping("/show/distritos")
    public ResponseEntity<?> MostrarDistritos() {

        Set<DistritoResponse> lista_distritos = new HashSet<>();

        distritoService.MostrarDistritos()
                .forEach(distrito -> lista_distritos.add(new DistritoResponse(
                        distrito.getIdDistrito(),
                        distrito.getNombreDistrito()
                )));

        return new ResponseEntity<>(lista_distritos,
                HttpStatus.OK);
    }

    @GetMapping("/find/distritos/from_provincia/{id_provincia}")
    public ResponseEntity<?> BuscarDistritosPorProvincia(@PathVariable("id_provincia") Long id_provincia) {

        Set<DistritoResponse> lista_distritos = new HashSet<>();

        distritoService.BuscarDistritos_By_IDProvincia(id_provincia)
                .forEach(distrito -> lista_distritos.add(new DistritoResponse(
                        distrito.getIdDistrito(),
                        distrito.getNombreDistrito()
                )));

        return new ResponseEntity<>(lista_distritos,
                HttpStatus.OK);
    }
}
