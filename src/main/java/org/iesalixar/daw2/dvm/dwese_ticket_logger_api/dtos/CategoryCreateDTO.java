package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase DTO (Data Transfer Object) utilizada para crear una nueva categoría.
 *
 * Esta clase se utiliza para transferir datos desde la API al servidor
 * al momento de crear una nueva categoría.
 */
@Getter
@Setter
public class CategoryCreateDTO {

    /**
     * Nombre de la categoría.
     * No puede estar vacío y debe tener entre 2 y 100 caracteres.
     * Ejemplo: "Electrónica", "Ropa".
     */
    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    private String name;

    /**
     * Identificador de la categoría padre.
     * Puede ser nulo si la categoría no tiene un padre (es una categoría raíz).
     */
    private Long parentCategoryId;


    private MultipartFile imageFile;
}
