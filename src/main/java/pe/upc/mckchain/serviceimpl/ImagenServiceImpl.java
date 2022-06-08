package pe.upc.mckchain.serviceimpl;

import org.springframework.stereotype.Service;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.repository.IImagenDAO;
import pe.upc.mckchain.service.IImagenService;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class ImagenServiceImpl implements IImagenService {

    final
    IImagenDAO data;

    public ImagenServiceImpl(IImagenDAO data) {
        this.data = data;
    }

    @Override
    public Optional<Imagen> BuscarImagen_By_IDImagen(UUID id_imagen) {
        return data.findById(id_imagen);
    }

    @Override
    public Optional<Imagen> BuscarImagen_By_NombreImagen(String nombre_imagen) {
        return data.findByNombreImagen(nombre_imagen);
    }

    @Override
    public void GuardarImagen(Imagen imagen) {
        data.save(imagen);
    }
}
