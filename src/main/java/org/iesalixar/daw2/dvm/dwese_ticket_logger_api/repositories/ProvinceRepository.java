package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio para la entidad Province que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Province.
 */
public interface ProvinceRepository extends JpaRepository<Province, Long> {

    /**
     * Comprueba si existe una provincia con un código específico.
     *
     * @param code el código de la provincia.
     * @return true si existe una provincia con el código especificado, false en caso contrario.
     */
    boolean existsByCode(String code);

    /**
     * Comprueba si existe una provincia con un código específico, excluyendo una provincia por su ID.
     *
     * @param code el código de la provincia.
     * @param id el ID de la provincia a excluir.
     * @return true si existe una provincia con el código especificado (excluyendo la provincia con el ID dado), false en caso contrario.
     */
    @Query("SELECT COUNT(p) > 0 FROM Province p WHERE p.code = :code AND p.id != :id")
    boolean existsProvinceByCodeAndNotId(@Param("code") String code, @Param("id") Long id);
}
