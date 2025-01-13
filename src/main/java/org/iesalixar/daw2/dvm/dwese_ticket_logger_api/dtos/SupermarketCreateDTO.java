package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;

import java.util.List;

/**
 * Clase DTO (Data Transfer Object) utilizada para crear un nuevo supermercado.
 *
 * Esta clase se utiliza para transferir datos desde la API al servidor
 * al momento de crear un nuevo supermercado.
 */
@Getter
@Setter
public class SupermarketCreateDTO {

    @NotEmpty(message = "{msg.supermarket.name.notEmpty}")
    @Size(max = 100, message = "{msg.supermarket.name.size}")
    private String name;

}
