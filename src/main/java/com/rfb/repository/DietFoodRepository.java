package com.rfb.repository;

import com.rfb.domain.DietFood;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the DietFood entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DietFoodRepository extends JpaRepository<DietFood, Long> {

}
