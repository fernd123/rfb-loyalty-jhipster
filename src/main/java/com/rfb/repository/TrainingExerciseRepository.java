package com.rfb.repository;

import com.rfb.domain.TrainingExercise;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TrainingExercise entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TrainingExerciseRepository extends JpaRepository<TrainingExercise, Long> {

}
