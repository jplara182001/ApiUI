package api.ui.ui;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class RespuestaLibros {
    @JsonProperty("Libros")
    private List<Libro> libros;

    @JsonProperty("NumberOfElements")
    private int numberOfElements;

    @JsonProperty("TotalElements")
    private int totalElements;

    @JsonProperty("TotalPages")
    private int totalPages;

    public int getTotalPages() {
        return totalPages;
    }

    public List<Libro> getLibros() {
        return libros;
    }

    public void setLibros(List<Libro> libros) {
        this.libros = libros;
    }

    @Override
    public String toString() {
        return "RespuestaLibros{" +
                "libros=" + libros +
                ", numberOfElements=" + numberOfElements +
                ", totalElements=" + totalElements +
                ", totalPages=" + totalPages +
                '}';
    }
}
