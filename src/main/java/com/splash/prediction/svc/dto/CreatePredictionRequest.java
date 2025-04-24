package com.splash.prediction.svc.dto;

import jakarta.validation.constraints.NotNull;

public record CreatePredictionRequest(
        @NotNull
        Long userId,
        @NotNull
        Long matchId,
        @NotNull
        String predictedWinner
) {
}
