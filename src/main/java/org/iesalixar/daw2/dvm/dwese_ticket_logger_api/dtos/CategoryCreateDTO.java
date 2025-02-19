package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * Clase DTO (Data Transfer Object) utilizada para crear una nueva categoría.
 *
 * Esta clase encapsula los datos necesarios para crear una entidad {@link Category},
 * asegurando la validación de los campos requeridos y permitiendo la transferencia
 * de archivos relacionados, como imágenes, desde la API al servidor.
 */
@Getter
@Setter
public class CategoryCreateDTO {

    /**
     * Nombre de la categoría.
     * <p>
     * - No puede estar vacío. <br>
     * - Debe tener entre 2 y 100 caracteres de longitud. <br>
     * - Ejemplo: "Electrónica", "Ropa".
     * </p>
     */
    @NotEmpty(message = "El nombre no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres.")
    private String name;

    /**
     * Identificador de la categoría padre.
     * <p>
     * - Puede ser nulo si la categoría no tiene un padre (es una categoría raíz). <br>
     * - Ejemplo: Si la categoría "Teléfonos" pertenece a "Electrónica",
     *   el ID de "Electrónica" sería el valor de este campo.
     * </p>
     */
    private Long parentCategoryId;

    /**
     * Archivo de imagen asociado a la categoría.
     * <p>
     * - Este archivo puede ser utilizado para almacenar un logotipo o imagen representativa de la categoría. <br>
     * - Es opcional, pero si se proporciona, debe ser un archivo válido de tipo {@link MultipartFile}.
     * </p>
     */
    private MultipartFile imageFile;
}
