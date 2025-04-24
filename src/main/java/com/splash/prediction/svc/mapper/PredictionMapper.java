package com.splash.prediction.svc.mapper;

import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.model.PredictionEntity;
import org.springframework.stereotype.Component;

@Component
public class PredictionMapper {

    public PredictionDto mapToDto(PredictionEntity predictionEntity) {
        return new PredictionDto(
                predictionEntity.getId(),
                predictionEntity.getUserId(),
                predictionEntity.getMatchId(),
                predictionEntity.getPredictedWinner(),
                predictionEntity.getOutcome());
    }
}
