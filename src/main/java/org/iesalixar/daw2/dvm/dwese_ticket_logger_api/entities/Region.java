package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * La clase `Region` representa una entidad que modela una región dentro de la base de datos.
 * Contiene tres campos: `id`, `code` y `name`, donde `id` es el identificador único de la región,
 * `code` es un código asociado a la región, y `name` es el nombre de la región.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "regions") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {


    // Campo que almacena el identificador único de la región.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Campo que almacena el código de la región, normalmente una cadena corta que identifica la región.
    // Ejemplo: "01" para Andalucía.
    // @NotEmpty(message = "{msg.region.code.notEmpty}")
    // @Size(max = 2, message = "{msg.region.code.size}")
    @Column(name = "code", nullable = false, length = 2) // Define la columna correspondiente en la tabla.
    private String code;


    // Campo que almacena el nombre completo de la región, como "Andalucía" o "Cataluña".
    // @NotEmpty(message = "{msg.region.name.notEmpty}")
    // @Size(max = 100, message = "{msg.region.name.size}")
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;


    // Relación uno a muchos con la entidad Province.
    // Una región puede tener muchas provincias.
    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Province> provinces;


    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Region` cuando no es necesario o no se conoce el `id` de la región
     * (por ejemplo, antes de insertar la región en la base de datos, donde el `id` es autogenerado).
     * @param code Código de la región.
     * @param name Nombre de la región.
     */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
