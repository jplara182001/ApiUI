package api.ui.ui;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LibroControlador {

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/libros")
    public String obtenerLibros(@RequestParam(defaultValue = "0") int page, Model model) {
        // URL de la API con los parámetros de paginación
        String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros?page=" + page + "&size=5";

        // Realizamos la petición GET y deserializamos el JSON en un objeto
        // RespuestaLibros
        RespuestaLibros respuestaLibros = restTemplate.getForObject(url, RespuestaLibros.class);

        // Verificamos que la respuesta sea válida
        if (respuestaLibros != null && respuestaLibros.getLibros() != null) {
            model.addAttribute("libros", respuestaLibros.getLibros());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", respuestaLibros.getTotalPages());
        } else {
            model.addAttribute("libros", List.of()); // Si no hay libros, pasamos una lista vacía
            model.addAttribute("currentPage", 0);
            model.addAttribute("totalPages", 0);
        }

        return "libros"; // nombre del template Thymeleaf
    }

    @GetMapping("/libros/crear")
    public String mostrarFormularioCrear(Model model) {
        model.addAttribute("libro", new Libro());
        return "crear";
    }

    @PostMapping("/libros/crear")
    public String crearLibro(@ModelAttribute Libro libro, HttpServletRequest request) {
        String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros";

        try {
            restTemplate.postForObject(url, libro, Libro.class);
            System.out.println("Libro creado correctamente: " + libro.getTitulo());
        } catch (Exception e) {
            System.err.println("Error al crear el libro: " + e.getMessage());
            e.printStackTrace();
        }

        String host = request.getHeader("Host");
        String baseUrl = request.getScheme() + "://" + host;

        String forwardedHost = request.getHeader("X-Forwarded-Host");
        if (forwardedHost != null && !forwardedHost.isEmpty()) {
            baseUrl = request.getScheme() + "://" + forwardedHost;
        }

        return "redirect:" + baseUrl + "/libros";
    }

    @GetMapping("/libros/editar/{id}")
public String mostrarFormularioEditar(@PathVariable String id, Model model) {
    String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros/" + id;

    try {
        // Obtener el JSON como un Map
        Map<String, Object> response = restTemplate.getForObject(url, Map.class);
        if (response == null || !response.containsKey("Libro")) {
            model.addAttribute("error", "El libro no fue encontrado.");
            return "error";
        }

        // Extraer el mapa que representa el libro
        Map<String, Object> libroMap = (Map<String, Object>) response.get("Libro");

        // Convertir ese mapa a objeto Libro
        ObjectMapper mapper = new ObjectMapper();
        Libro libro = mapper.convertValue(libroMap, Libro.class);

        model.addAttribute("libro", libro);
        model.addAttribute("libroId", id);

    } catch (Exception e) {
        model.addAttribute("error", "Error al obtener el libro: " + e.getMessage());
        return "error";
    }

    return "editar";
}


    // Método para procesar la edición utilizando PostMapping
    @PostMapping("/libros/actualizar/{id}")
    public String procesarEdicion(@PathVariable String id, @ModelAttribute Libro libro,
            HttpServletRequest request) {
        String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros/" + id;

        // Asegúrate de que el libro tenga el ID correcto
        libro.setId(id);

        try {
            restTemplate.put(url, libro);
            System.out.println("Libro actualizado correctamente: " + libro.getTitulo());
        } catch (Exception e) {
            System.err.println("Error al actualizar el libro: " + e.getMessage());
            e.printStackTrace();
        }

        // Solución simple usando el encabezado Host
        String host = request.getHeader("Host");
        String baseUrl = request.getScheme() + "://" + host;

        // Si estamos detrás de un proxy, podríamos considerar otros encabezados
        String forwardedHost = request.getHeader("X-Forwarded-Host");
        if (forwardedHost != null && !forwardedHost.isEmpty()) {
            baseUrl = request.getScheme() + "://" + forwardedHost;
        }

        System.out.println("Redirigiendo a: " + baseUrl + "/libros");
        return "redirect:" + baseUrl + "/libros";
    }

    @DeleteMapping("/libros/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminarLibro(@PathVariable String id) {
        String url = "https://crudlibrosaiven-production.up.railway.app/api/v0/libros/" + id;
        restTemplate.delete(url);
        return ResponseEntity.ok().build();
    }
}
