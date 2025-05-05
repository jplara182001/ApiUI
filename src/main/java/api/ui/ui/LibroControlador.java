package api.ui.ui;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

@Controller
public class LibroControlador {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/libros")
    public String obtenerLibros(Model model) {
        // URL de la API desde donde obtenemos los libros
        String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros"; // Asegúrate de que esta URL sea correcta

        // Realizamos la petición GET y deserializamos el JSON en un objeto RespuestaLibros
        RespuestaLibros respuestaLibros = restTemplate.getForObject(url, RespuestaLibros.class);

        System.out.println(respuestaLibros); // Esto te ayudará a ver lo que está devolviendo la API

        // Verificamos que se haya deserializado correctamente
        if (respuestaLibros != null && respuestaLibros.getLibros() != null) {
            model.addAttribute("libros", respuestaLibros.getLibros());
        } else {
            model.addAttribute("libros", List.of());  // Si no hay libros, pasamos una lista vacía
        }
        
        return "libros";  // nombre del template Thymeleaf
    }
}
