package com.splash.prediction.svc.service;

import com.splash.prediction.svc.dto.CreatePredictionRequest;
import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.dto.UpdatePredictionRequest;
import com.splash.prediction.svc.repository.PredictionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepo predictionRepo;

    public PredictionDto createPrediction(CreatePredictionRequest createPredictionRequest) {
        return null;
    }

    public PredictionDto updatePrediction(UpdatePredictionRequest updatePredictionRequest) {

        return null;
    }

    public List<PredictionDto> getAllPredictionsByUserId(Long userId) {

        return List.of();
    }
}
