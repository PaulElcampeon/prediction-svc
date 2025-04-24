package com.splash.prediction.svc.dto;

import com.splash.prediction.svc.enums.PredictionOutcome;

public record PredictionDto(
        Long predictionId,
        Long userId,
        Long matchId,
        String predictedWinner,
        PredictionOutcome outcome
) {
}
