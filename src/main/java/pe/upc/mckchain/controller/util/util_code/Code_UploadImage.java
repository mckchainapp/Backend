package pe.upc.mckchain.controller.util.util_code;

import org.apache.commons.io.IOUtils;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pe.upc.mckchain.model.Imagen;
import pe.upc.mckchain.model.Usuario;
import pe.upc.mckchain.service.IImagenService;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class Code_UploadImage {

    public static void AssignImage(Usuario usuario, String path, InputStream fotoStream, IImagenService imagenService)
            throws IOException {

        String nombre_foto = UUID.randomUUID() + "-" + UUID.randomUUID() + ".png";

        String url_foto = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .path(nombre_foto)
                .toUriString();

        assert fotoStream != null;
        byte[] file_foto = IOUtils.toByteArray(fotoStream);

        Imagen imagen = new Imagen(
                nombre_foto,
                "image/png",
                url_foto,
                file_foto,
                usuario
        );

        imagenService.GuardarImagen(imagen);
    }

    public static String UpdateImageName(String nombre_foto) {

        //Obteniendo Nombre del Archivo Actual
        String[] separador_nombre = nombre_foto.split("\\.");

        return separador_nombre[0];
    }

    public static String SendFileUrl(String path, String nombre_archivo) {

        return ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path(path)
                .path(nombre_archivo)
                .toUriString();
    }
}
