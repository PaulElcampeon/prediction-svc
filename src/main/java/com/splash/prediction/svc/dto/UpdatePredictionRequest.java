package com.splash.prediction.svc.dto;

public record UpdatePredictionRequest(
        Long predictionId,
        String predictedWinner
) {
}
