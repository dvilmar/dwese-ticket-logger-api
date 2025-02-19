package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio para la entidad Region que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Region.
 */
public interface RegionRepository extends JpaRepository<Region, Long> {

    /**
     * Obtiene todas las regiones.
     *
     * @return una lista de todas las regiones.
     */
    List<Region> findAll();

    /**
     * Inserta o actualiza una región.
     *
     * @param region la entidad Region a insertar o actualizar.
     * @return la entidad Region insertada o actualizada.
     */
    Region save(Region region);

    /**
     * Elimina una región por su ID.
     *
     * @param id el ID de la región a eliminar.
     */
    void deleteById(Long id);

    /**
     * Obtiene una región por su ID.
     *
     * @param id el ID de la región.
     * @return un Optional que contiene la región si se encuentra, o vacío si no se encuentra.
     */
    Optional<Region> findById(Long id);

    /**
     * Comprueba si existe una región con un código específico.
     *
     * @param code el código de la región.
     * @return true si existe una región con el código especificado, false en caso contrario.
     */
    boolean existsByCode(String code);

    /**
     * Comprueba si existe una región con un código específico, excluyendo una región por su ID.
     *
     * @param code el código de la región.
     * @param id el ID de la región a excluir.
     * @return true si existe una región con el código especificado (excluyendo la región con el ID dado), false en caso contrario.
     */
    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code AND r.id != :id")
    boolean existsRegionByCodeAndNotId(@Param("code") String code, @Param("id") Long id);
}
