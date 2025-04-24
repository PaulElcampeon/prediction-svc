package com.splash.prediction.svc.controller;

import com.splash.prediction.svc.dto.CreatePredictionRequest;
import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.dto.UpdatePredictionRequest;
import com.splash.prediction.svc.service.PredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/prediction")
@RequiredArgsConstructor
public class PredictionController {

    private final PredictionService predictionService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PredictionDto>> getAllPredictionsByUserId(@PathVariable Long userId) {
        List<PredictionDto> predictionDtos = predictionService.getAllPredictionsByUserId(userId);
        return new ResponseEntity<>(predictionDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PredictionDto> createPrediction(@Valid @RequestBody CreatePredictionRequest createPredictionRequest) {
        PredictionDto predictionDto = predictionService.createPrediction(createPredictionRequest);
        return new ResponseEntity<>(predictionDto, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<PredictionDto> updatePrediction(@Valid @RequestBody UpdatePredictionRequest updatePredictionRequest) {
        PredictionDto predictionDto = predictionService.updatePrediction(updatePredictionRequest);
        return new ResponseEntity<>(predictionDto, HttpStatus.OK);
    }
}
