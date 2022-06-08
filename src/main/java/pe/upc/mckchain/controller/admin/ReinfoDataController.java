package pe.upc.mckchain.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.upc.mckchain.dto.request.reinfo.ReinfoSingleDataRequest;
import pe.upc.mckchain.dto.request.reinfo.ReinfoUploadDataRequest;
import pe.upc.mckchain.dto.response.ReinfoResponse;
import pe.upc.mckchain.dto.response.general.MessageResponse;
import pe.upc.mckchain.model.Departamento;
import pe.upc.mckchain.model.Distrito;
import pe.upc.mckchain.model.Minera;
import pe.upc.mckchain.model.Provincia;
import pe.upc.mckchain.service.IDepartamentoService;
import pe.upc.mckchain.service.IDistritoService;
import pe.upc.mckchain.service.IMineraService;
import pe.upc.mckchain.service.IProvinciaService;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class ReinfoDataController {

    final
    IMineraService mineraService;

    final
    IDepartamentoService departamentoService;

    final
    IProvinciaService provinciaService;

    final
    IDistritoService distritoService;

    public ReinfoDataController(IMineraService mineraService, IDepartamentoService departamentoService,
                                IProvinciaService provinciaService, IDistritoService distritoService) {
        this.mineraService = mineraService;
        this.departamentoService = departamentoService;
        this.provinciaService = provinciaService;
        this.distritoService = distritoService;
    }

    @PostMapping("/reinfo/data/upload")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> SubirDatosReinfo(@RequestBody ReinfoUploadDataRequest reinfoUploadDataRequest) {

        for (ReinfoSingleDataRequest reinfo_minera : reinfoUploadDataRequest.getReinfoData()) {

            Optional<Departamento> departamento_data =
                    departamentoService.BuscarDepartamento_By_NombreDepartamento(reinfo_minera.getDepartamentoMinera());

            if (departamento_data.isPresent()) {
                Departamento departamento = departamento_data.get();

                Optional<Provincia> provincia_data = provinciaService.BuscarProvincia_ByIDDepartamentoAndNombreProvincia(
                        departamento.getIdDepartamento(),
                        reinfo_minera.getProvinciaMinera()
                );

                if (provincia_data.isPresent()) {
                    Provincia provincia = provincia_data.get();

                    Optional<Distrito> distrito_data = distritoService.BuscarDistrito_By_IDProvinciaAndNombreDistrito(
                            provincia.getIdProvincia(),
                            reinfo_minera.getDistritoMinera()
                    );

                    if (distrito_data.isPresent()) {
                        Distrito distrito = distrito_data.get();

                        Optional<Minera> minera_data = mineraService.BuscarMinera_By_CodigoUnicoMineraAndRucMinera(
                                reinfo_minera.getCodigounicoMinera(),
                                reinfo_minera.getRucMinera());

                        if (minera_data.isPresent()) {
                            Minera current_minera = minera_data.get();

                            current_minera.setNombreMinera(reinfo_minera.getNombreMinera());
                            current_minera.setEstadoactividadMinera(reinfo_minera.getEstadoactividadMinera());
                            current_minera.setDistritoMinera(distrito);

                            mineraService.GuardarMinera(current_minera);

                            System.out.println("Se actualizó la Minera satisfactoriamente.");
                        } else {
                            Minera minera = new Minera(
                                    reinfo_minera.getCodigounicoMinera(),
                                    reinfo_minera.getEstadoactividadMinera(),
                                    reinfo_minera.getRucMinera(),
                                    reinfo_minera.getNombreMinera(),
                                    distrito
                            );
                            mineraService.GuardarMinera(minera);

                            System.out.println("Se registró la Minera satisfactoriamente.");
                        }
                    } else {
                        return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Distrito ingresado."),
                                HttpStatus.NOT_FOUND);
                    }
                } else {
                    return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar la Provincia ingresada."),
                            HttpStatus.NOT_FOUND);
                }
            } else {
                return new ResponseEntity<>(new MessageResponse("Ocurrió un error al buscar el Departamento ingresado."),
                        HttpStatus.NOT_FOUND);
            }
        }

        return new ResponseEntity<>(new MessageResponse("Se realizó el Registro de REINFO satisfactoriamente."),
                HttpStatus.ACCEPTED);
    }

    @GetMapping("/reinfo/data/display")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_COMPRADOR')")
    public ResponseEntity<?> MostrarDatosReinfo() {

        Set<ReinfoResponse> lista_mineras = new HashSet<>();

        mineraService.MostrarMineras()
                .forEach(minera -> lista_mineras.add(new ReinfoResponse(
                        minera.getCodigounicoMinera(),
                        minera.getEstadoactividadMinera(),
                        minera.getRucMinera(),
                        minera.getNombreMinera(),
                        SendNombreDepartamento(minera.getDistritoMinera()),
                        SendNombreProvincia(minera.getDistritoMinera()),
                        SendNombreDistrito(minera.getDistritoMinera())
                )));

        return new ResponseEntity<>(lista_mineras,
                HttpStatus.OK);
    }

    String SendNombreDistrito(Distrito distrito) {

        return distrito.getNombreDistrito();
    }

    String SendNombreProvincia(Distrito distrito) {

        Provincia provincia = provinciaService.BuscarProvincia_By_IDDistrito(distrito.getIdDistrito())
                .orElse(null);

        assert provincia != null;
        return provincia.getNombreProvincia();
    }

    String SendNombreDepartamento(Distrito distrito) {

        Provincia provincia = provinciaService.BuscarProvincia_By_IDDistrito(distrito.getIdDistrito())
                .orElse(null);

        assert provincia != null;
        Departamento departamento = departamentoService.BuscarDepartamento_By_IDProvincia(provincia.getIdProvincia())
                .orElse(null);

        assert departamento != null;
        return departamento.getNombreDepartamento();
    }
}
