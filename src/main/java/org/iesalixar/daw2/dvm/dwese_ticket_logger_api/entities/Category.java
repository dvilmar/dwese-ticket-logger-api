package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;


import jakarta.annotation.Nullable;
import jakarta.persistence.*; // Anotaciones de JPA
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * La clase `Category` representa una entidad que modela una categoría dentro de la base de datos.
 * Contiene tres campos: `id`, `image` y `name`, donde `id` es el identificador único de la categoría,
 * `image` es una imagen asociado a la categoría, y `name` es el nombre de la categoría.
 *
 * Las anotaciones de Lombok ayudan a reducir el código repetitivo al generar automáticamente
 * métodos comunes como getters, setters, constructores, y otros métodos estándar de los objetos.
 */
@Entity // Marca esta clase como una entidad gestionada por JPA.
@Table(name = "categories") // Especifica el nombre de la tabla asociada a esta entidad.
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {


    // Campo que almacena el identificador único de la categoría.
    // Es una clave primaria autogenerada por la base de datos.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true) // Clave foránea a la tabla supermercados.
    private Category parent;


    // Campo que almacena el nombre completo de la categoría.
    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Size(max = 100, message = "{msg.category.name.size}")
    @Column(name = "name", nullable = false, length = 100) // Define la columna correspondiente en la tabla.
    private String name;


    // Campo que almacena la imagen de la categoría.
    @Column(name = "image", nullable = true, length = 100) // Define la columna correspondiente en la tabla.
    private String image;


    /**
     * Este es un constructor personalizado que no incluye el campo `id`.
     * Se utiliza para crear instancias de `Category` cuando no es necesario o no se conoce el `id` de la categoría
     * (por ejemplo, antes de insertar la categoría en la base de datos, donde el `id` es autogenerado).
     * @param name Nombre de la categoría.
     * @param image Imagen de la categoría.
     */
    public Category(String name, @Nullable String image, @Nullable Category parent) {
        this.name = name;
        this.image = image;
        this.parent = parent;
    }
}
