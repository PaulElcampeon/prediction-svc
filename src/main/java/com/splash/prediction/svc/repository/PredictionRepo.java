package com.splash.prediction.svc.repository;

import com.splash.prediction.svc.model.PredictionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PredictionRepo extends JpaRepository<PredictionEntity, Long> {
    List<PredictionEntity> findAllByUserId(Long userId);
}
