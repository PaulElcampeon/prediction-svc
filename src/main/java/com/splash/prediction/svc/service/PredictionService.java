package com.splash.prediction.svc.service;

import com.splash.prediction.svc.dto.CreatePredictionRequest;
import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.dto.UpdatePredictionRequest;
import com.splash.prediction.svc.mapper.PredictionMapper;
import com.splash.prediction.svc.model.PredictionEntity;
import com.splash.prediction.svc.repository.PredictionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionService {

    private final PredictionRepo predictionRepo;
    private final PredictionMapper predictionMapper;

    public PredictionDto createPrediction(CreatePredictionRequest createPredictionRequest) {
        PredictionEntity newPrediction = PredictionEntity.builder()
                .userId(createPredictionRequest.userId())
                .matchId(createPredictionRequest.matchId())
                .predictedWinner(createPredictionRequest.predictedWinner())
                .build();

        newPrediction = predictionRepo.save(newPrediction);

        return predictionMapper.mapToDto(newPrediction);
    }

    public PredictionDto updatePrediction(UpdatePredictionRequest updatePredictionRequest) {
        PredictionEntity existingPrediction = predictionRepo.findById(updatePredictionRequest.predictionId())
                .orElseThrow(() -> new RuntimeException("Prediction not found"));

        existingPrediction.setPredictedWinner(updatePredictionRequest.predictedWinner());

        existingPrediction = predictionRepo.save(existingPrediction);

        return predictionMapper.mapToDto(existingPrediction);
    }

    public List<PredictionDto> getAllPredictionsByUserId(Long userId) {
        List<PredictionEntity> predictions = predictionRepo.findAllByUserId(userId);

        return predictions.stream()
                .map(predictionMapper::mapToDto)
                .toList();
    }
}
