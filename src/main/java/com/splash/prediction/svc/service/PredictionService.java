package com.splash.prediction.svc.service;

import com.splash.prediction.svc.dto.CreatePredictionRequest;
import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.dto.UpdatePredictionRequest;
import com.splash.prediction.svc.enums.PredictionState;
import com.splash.prediction.svc.mapper.PredictionMapper;
import com.splash.prediction.svc.model.PredictionEntity;
import com.splash.prediction.svc.repository.PredictionRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
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

        log.info("Created new prediction: {}", newPrediction);

        return predictionMapper.mapToDto(newPrediction);
    }

    public PredictionDto updatePrediction(UpdatePredictionRequest updatePredictionRequest) {
        PredictionEntity existingPrediction = predictionRepo.findById(updatePredictionRequest.predictionId())
                .orElseThrow(() -> new RuntimeException("Prediction not found"));

        if (!isPredictionOpen(existingPrediction)) {
            throw new RuntimeException("Cannot update a prediction that is not open");
        }

        existingPrediction.setPredictedWinner(updatePredictionRequest.predictedWinner());

        existingPrediction = predictionRepo.save(existingPrediction);

        log.info("Updated prediction: {}", existingPrediction);

        return predictionMapper.mapToDto(existingPrediction);
    }

    public List<PredictionDto> getAllPredictionsByUserId(Long userId) {
        List<PredictionEntity> predictions = predictionRepo.findAllByUserId(userId);

        log.info("Fetched {} predictions for userId: {}", predictions.size(), userId);

        return predictions.stream()
                .map(predictionMapper::mapToDto)
                .toList();
    }

    private boolean isPredictionOpen(PredictionEntity prediction) {
        return prediction.getState() == PredictionState.OPEN;
    }
}
