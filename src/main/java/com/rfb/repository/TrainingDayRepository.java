package com.rfb.repository;

import com.rfb.domain.TrainingDay;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrainingDay entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingDayRepository extends JpaRepository<TrainingDay, Long> {

}
