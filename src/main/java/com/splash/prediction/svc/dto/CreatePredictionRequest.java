package com.splash.prediction.svc.dto;

public record CreatePredictionRequest(
        Long userId,
        Long matchId,
        String predictedWinner
) {
}
