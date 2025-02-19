package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * La clase `Location` representa una entidad que modela una ubicación.
 * Contiene tres campos: `id`, `address`, `city`, `supermarket` y `province`,
 * donde `id` es el identificador único de la ubicación,
 * `address` es la dirección, `city` es la ciudad, `supermarket` es la referencia
 * al supermercado al que pertenece la ubicación, y `province` es la provincia a la que pertenece.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "locations") // Especifica el nombre de la tabla asociada a esta entidad.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"supermarket", "province"}) // Evita recursión en las relaciones bidireccionales.
@EqualsAndHashCode(exclude = {"supermarket", "province"}) // Excluye para evitar problemas de recursión.
public class Location {

    // Identificador único de la ubicación. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Dirección de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.address.notEmpty}")
    @Column(name = "address", nullable = false)
    private String address;

    // Ciudad de la ubicación. No puede estar vacía.
    @NotEmpty(message = "{msg.location.city.notEmpty}")
    @Column(name = "city", nullable = false)
    private String city;

    // Relación con el supermercado al que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.supermarket.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id", nullable = false) // Clave foránea a la tabla supermercados.
    private Supermarket supermarket;

    // Relación con la provincia a la que pertenece la ubicación. No puede ser nulo.
    @NotNull(message = "{msg.location.province.notNull}")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "province_id", nullable = false) // Clave foránea a la tabla provincias.
    private Province province;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Location`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarla en la base de datos).
     * @param address Dirección de la ubicación.
     * @param city Ciudad de la ubicación.
     * @param supermarket Supermercado al que pertenece la ubicación.
     * @param province Provincia a la que pertenece la ubicación.
     */
    public Location(String address, String city, Supermarket supermarket, Province province) {
        this.address = address;
        this.city = city;
        this.supermarket = supermarket;
        this.province = province;
    }
}
