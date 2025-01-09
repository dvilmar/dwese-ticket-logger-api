package org.iesalixar.daw2.dvm.dwese_ticket_logger_api.repositories;


import org.iesalixar.daw2.dvm.dwese_ticket_logger_api.entities.Province;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ProvinceRepository extends JpaRepository<Province, Long> {


    boolean existsByCode(String code);
    @Query("SELECT COUNT(r) > 0 FROM Region r WHERE r.code = :code AND r.id != :id")
    boolean existsProvinceByCodeAndNotId(@Param("code") String code, @Param("id") Long id);

}
