package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * La clase `Product` representa una entidad que modela un producto.
 * Contiene campos como `id`, `name`, `price` y `category`,
 * donde `id` es el identificador único del producto,
 * `name` es el nombre del producto, `price` es el precio del producto, y `category` es la categoría asociada.
 */
@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"category", "tickets"}) // Excluye `category` y `tickets` para evitar ciclos recursivos.
@EqualsAndHashCode(exclude = {"category", "tickets"}) // Excluye `category` y `tickets` para evitar problemas de recursión.
public class Product {

    // Identificador único del producto. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nombre del producto. No puede estar vacío y debe tener entre 2 y 100 caracteres.
    @NotEmpty(message = "{msg.product.name.notEmpty}")
    @Size(min = 2, max = 100, message = "{msg.product.name.size}")
    @Column(name = "name", nullable = false)
    private String name;

    // Precio del producto. No puede ser nulo.
    @NotNull(message = "{msg.product.price.notNull}")
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    // Categoría asociada al producto.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    // Lista de tickets asociados al producto.
    @ManyToMany(mappedBy = "products")
    private List<Ticket> tickets;
}
