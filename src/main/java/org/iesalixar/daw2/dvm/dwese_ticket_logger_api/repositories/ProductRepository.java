package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;

import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositorio para la entidad Product que extiende JpaRepository.
 * Proporciona operaciones CRUD y consultas personalizadas para la entidad Product.
 */
public interface ProductRepository extends JpaRepository<Product, Long> {


    /**
     * Obtiene todos los productos que pertenecen a una categoría específica.
     *
     * @param categoryId el ID de la categoría.
     * @return una lista de productos que pertenecen a la categoría especificada.
     */
    List<Product> findByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * Busca una lista de productos cuyo nombre contenga la cadena proporcionada, sin distinguir entre mayúsculas y minúsculas.
     *
     * @param name la cadena a buscar dentro del campo 'name' de los productos.
     * @return una lista de productos que contienen la cadena especificada, ignorando mayúsculas y minúsculas.
     */
    List<Product> findByNameContainingIgnoreCase(String name);
}
