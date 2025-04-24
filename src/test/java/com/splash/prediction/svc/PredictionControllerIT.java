package com.splash.prediction.svc;

import com.splash.prediction.svc.dto.CreatePredictionRequest;
import com.splash.prediction.svc.dto.PredictionDto;
import com.splash.prediction.svc.dto.UpdatePredictionRequest;
import com.splash.prediction.svc.enums.PredictionOutcome;
import com.splash.prediction.svc.model.PredictionEntity;
import com.splash.prediction.svc.repository.PredictionRepo;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PredictionControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    private TestRestTemplate restTemplate;

    @Autowired
    private PredictionRepo predictionRepo;
    @Value("${local.server.port}")
    private int port;
    private String baseUrl;


    @BeforeAll
    public static void init() {
        System.setProperty("spring.datasource.url", postgresContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", postgresContainer.getUsername());
        System.setProperty("spring.datasource.password", postgresContainer.getPassword());
        System.setProperty("spring.datasource.driver-class-name", postgresContainer.getDriverClassName());
        System.setProperty("spring.jpa.properties.hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
    }

    @BeforeEach
    void setUp() {
        restTemplate = new TestRestTemplate();
        baseUrl = "http://localhost:" + port + "/api/v1/prediction";
        predictionRepo.deleteAll();
    }

    @Test
    void createPrediction_ShouldReturnCreatedPrediction() {
        CreatePredictionRequest request = new CreatePredictionRequest(1L, 101L, "Team A");

        ResponseEntity<PredictionDto> response = restTemplate.postForEntity(baseUrl, request, PredictionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        PredictionDto body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.matchId()).isEqualTo(101L);
        assertThat(body.predictedWinner()).isEqualTo("Team A");

        List<PredictionEntity> all = predictionRepo.findAll();

        assertThat(all.size()).isEqualTo(1);
        PredictionEntity predictionEntity = all.get(0);

        assertThat(predictionEntity.getUserId()).isEqualTo(1L);
        assertThat(predictionEntity.getMatchId()).isEqualTo(101L);
        assertThat(predictionEntity.getPredictedWinner()).isEqualTo("Team A");
        assertThat(predictionEntity.getOutcome()).isEqualTo(PredictionOutcome.AWAITING);
    }

    @Test
    void updatePrediction_ShouldReturnUpdatedPrediction() {
        PredictionEntity saved = predictionRepo.save(PredictionEntity.builder()
                .userId(1L)
                .matchId(102L)
                .predictedWinner("Team B")
                .outcome(PredictionOutcome.AWAITING)
                .build());

        UpdatePredictionRequest updateRequest = new UpdatePredictionRequest(saved.getId(), "Team C");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<UpdatePredictionRequest> requestEntity = new HttpEntity<>(updateRequest, headers);

        ResponseEntity<PredictionDto> response = restTemplate.exchange(baseUrl, HttpMethod.PUT, requestEntity, PredictionDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().predictedWinner()).isEqualTo("Team C");
    }

    @Test
    void getAllPredictionsByUserId_ShouldReturnUserPredictions() {
        predictionRepo.save(PredictionEntity.builder()
                .userId(1L)
                .matchId(201L)
                .predictedWinner("Team X")
                .outcome(PredictionOutcome.AWAITING)
                .build());

        predictionRepo.save(PredictionEntity.builder()
                .userId(1L)
                .matchId(202L)
                .predictedWinner("Team Y")
                .outcome(PredictionOutcome.AWAITING)
                .build());

        String url = baseUrl + "/user/1";
        ResponseEntity<PredictionDto[]> response = restTemplate.getForEntity(url, PredictionDto[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    void createPrediction_ShouldFailValidation_WhenMissingFields() {
        CreatePredictionRequest invalidRequest = new CreatePredictionRequest(null, null, null);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<CreatePredictionRequest> requestEntity = new HttpEntity<>(invalidRequest, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(baseUrl, requestEntity, String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}