package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Repositorio para la entidad Location que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Location.
 */
public interface LocationRepository extends JpaRepository<Location, Long> {



    /**
     * Comprueba si existe una ubicación con una dirección específica.
     *
     * @param address la dirección de la ubicación.
     * @return true si existe una ubicación con la dirección especificada, false en caso contrario.
     */
    boolean existsByAddress(String address);

    /**
     * Comprueba si existe una ubicación con una dirección específica, excluyendo una ubicación por su ID.
     *
     * @param address la dirección de la ubicación.
     * @param id el ID de la ubicación a excluir.
     * @return true si existe una ubicación con la dirección especificada (excluyendo la ubicación con el ID dado), false en caso contrario.
     */
    @Query("SELECT COUNT(l) > 0 FROM Location l WHERE l.address = :address AND l.id != :id")
    boolean existsLocationByAddressAndNotId(@Param("address") String address, @Param("id") Long id);

}
