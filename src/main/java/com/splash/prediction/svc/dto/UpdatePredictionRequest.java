package com.splash.prediction.svc.dto;

import jakarta.validation.constraints.NotNull;

public record UpdatePredictionRequest(
        @NotNull
        Long predictionId,
        @NotNull
        String predictedWinner
) {
}
