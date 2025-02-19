package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio para la entidad Supermarket que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Supermarket.
 */
public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

    /**
     * Comprueba si existe un supermercado con un nombre específico.
     *
     * @param name el nombre del supermercado.
     * @return true si existe un supermercado con el nombre especificado, false en caso contrario.
     */
    boolean existsByName(String name);

    /**
     * Comprueba si existe un supermercado con un nombre específico, excluyendo un supermercado por su ID.
     *
     * @param name el nombre del supermercado.
     * @param id el ID del supermercado a excluir.
     * @return true si existe un supermercado con el nombre especificado (excluyendo el supermercado con el ID dado), false en caso contrario.
     */
    @Query("SELECT COUNT(s) > 0 FROM Supermarket s WHERE s.name = :name AND s.id != :id")
    boolean existsSupermarketByNameAndNotId(@Param("name") String name, @Param("id") Long id);

}
