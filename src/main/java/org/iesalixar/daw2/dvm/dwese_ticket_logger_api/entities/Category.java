package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

/**
 * La clase `Category` representa una entidad que modela una categoría.
 * Contiene tres campos: `id`, `name`, `image`, y `parentCategory`,
 * donde `id` es el identificador único de la categoría,
 * `name` es el nombre de la categoría, `image` es el enlace a una imagen,
 * y `parentCategory` es la categoría padre, permitiendo la relación jerárquica entre categorías.
 */
@Entity
@Table(name = "categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "subCategories") // Evita recursión en relaciones bidireccionales.
@EqualsAndHashCode(exclude = {"parentCategory", "subCategories"}) // Evita recursión en equals y hashCode.
public class Category {

    // Identificador único de la categoría. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre de la categoría. No puede estar vacío y debe tener entre 2 y 100 caracteres.
    @NotEmpty(message = "{msg.category.name.notEmpty}")
    @Size(min = 2, max = 100, message = "{msg.category.name.size}")
    @Column(name = "name", nullable = false)
    private String name;

    // Imagen asociada a la categoría. Debe tener una longitud máxima de 500 caracteres.
    @Size(max = 500, message = "{msg.category.image.size}")
    @Column(name = "image", nullable = true)
    private String image;

    // Relación autorreferencial con la categoría padre.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // Clave foránea que apunta a la categoría padre.
    private Category parentCategory;

    // Lista de subcategorías, mapeada con la relación bidireccional.
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Category> subCategories;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Category`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     * @param name Nombre de la categoría.
     * @param image Imagen asociada a la categoría.
     * @param parentCategory Categoría padre (puede ser nulo para categorías principales).
     */
    public Category(String name, String image, Category parentCategory) {
        this.name = name;
        this.image = image;
        this.parentCategory = parentCategory;
    }
}
