package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;


/**
 * La clase `Ticket` representa una entidad que modela un ticket.
 * Contiene campos como `id`, `date`, `discount` y `location`,
 * donde `id` es el identificador único del ticket,
 * `date` es la fecha del ticket, `discount` es el descuento aplicado,
 * y `location` representa la ubicación asociada al ticket.
 */
@Entity
@Table(name = "tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"location", "products"}) // Excluir relaciones para evitar recursión infinita.
@EqualsAndHashCode(exclude = {"location", "products"}) // Evitar bucles recursivos en equals y hashCode.
public class Ticket {


    // Identificador único del ticket. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Fecha del ticket. No puede ser nula.
    @NotNull(message = "{msg.ticket.date.notNull}")
    @Temporal(TemporalType.TIMESTAMP)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    @Column(name = "date", nullable = false)
    private Date date;


    // Descuento aplicado al ticket. No puede ser nulo.
    @NotNull(message = "{msg.ticket.discount.notNull}")
    @Column(name = "discount", nullable = false, precision = 5, scale = 2)
    private BigDecimal discount;


    // Ubicación asociada al ticket.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id", nullable = false)
    private Location location;


    // Lista de productos asociados al ticket.
    @ManyToMany
    @JoinTable(
            name = "product_ticket",
            joinColumns = @JoinColumn(name = "ticket_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products;


    /**
     * Calcula el total del ticket sumando los precios de todos los productos.
     *
     * @return el total calculado de los productos menos el descuento.
     */
    @Transient
    public BigDecimal getTotal() {
        if (products == null || products.isEmpty()) {
            return BigDecimal.ZERO;
        }


        BigDecimal total = BigDecimal.ZERO;
        for (Product product : products) {
            total = total.add(product.getPrice());
        }


        // Aplicar descuento si existe
        if (discount != null && discount.compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal discountPercentage = discount.divide(BigDecimal.valueOf(100));
            total = total.subtract(total.multiply(discountPercentage));
        }


        return total.setScale(2, RoundingMode.HALF_UP);
    }
}
