package api.ui.ui;

import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LibroService {
    private final String BACKEND_URL = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros";

    public List<Libro> obtenerLibros() {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.exchange(
                BACKEND_URL,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Libro>>() {}
        ).getBody();
    }
}
