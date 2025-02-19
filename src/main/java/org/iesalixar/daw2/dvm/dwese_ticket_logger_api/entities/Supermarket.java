package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

/**
 * La clase `Supermarket` representa una entidad que modela un supermercado.
 * Contiene dos campos: `id` y `name`, donde `id` es el identificador único del supermercado,
 * y `name` es el nombre del supermercado.
 */
@Entity // Marca esta clase como una entidad JPA.
@Table(name = "supermarkets") // Especifica el nombre de la tabla asociada a esta entidad.
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"locations"}) // Excluir `locations` para evitar ciclos recursivos.
@EqualsAndHashCode(exclude = {"locations"}) // Excluir `locations` para evitar problemas de recursión.
public class Supermarket {

    // Identificador único del supermercado. Es autogenerado y clave primaria.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Generación automática del ID.
    private Long id;

    // Nombre del supermercado. No puede estar vacío.
    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Column(name = "name", nullable = false) // Define la columna correspondiente en la tabla.
    private String name;

    // Relación uno a muchos con la entidad `Location`. Un supermercado puede tener muchas ubicaciones.
    @OneToMany(mappedBy = "supermarket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Location> locations;

    /**
     * Constructor que excluye el campo `id`. Se utiliza para crear instancias de `Supermarket`
     * cuando el `id` aún no se ha generado (por ejemplo, antes de insertarlo en la base de datos).
     * @param name Nombre del supermercado.
     */
    public Supermarket(String name) {
        this.name = name;
    }
}
