package com.cashrich.service;

import com.cashrich.entity.CoinEntity;
import com.cashrich.repository.CoinRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.time.LocalDateTime;

@Service
@Log4j2
public class ThirdPartyAPIService {

    @Value("${api.key}")
    private String apiKey;

    @Value("${coinmarketcap.api.url}")
    private  String apiUrl;

    private final RestTemplate restTemplate;
    private final CoinRepository coinRepository;


    public ThirdPartyAPIService(RestTemplate restTemplate, CoinRepository coinRepository) {
        this.restTemplate = restTemplate;
        this.coinRepository = coinRepository;
    }

    public String callCoinApi(String userId) {
        log.info("[ThirdPartyAPIService][callCoinApi] Calling Coin API for userId: {}", userId);


        HttpHeaders headers = new HttpHeaders();
            headers.set("X-CMC_PRO_API_KEY", apiKey);
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> entity = new HttpEntity<>(headers);

            ResponseEntity<String> responseEntity;
            try {
                responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, String.class);
                log.info("[ThirdPartyAPIService][callCoinApi] Successfully received response from Coin API for userId: {}", userId);

            } catch (Exception e) {

                throw new RuntimeException("API call failed: " + e.getMessage());
            }

            String response = responseEntity.getBody();

            saveApiCallLog(userId, apiUrl, userId, response);

            return response;
        }
        private void saveApiCallLog(String userId, String requestUrl, String requestPayload, String responsePayload) {
            log.info("[ThirdPartyAPIService][saveApiCallLog] API call log saved for userId: {}", userId);
            CoinEntity log = new CoinEntity();
            log.setUserId(userId);
            log.setRequestUrl(requestUrl);
            log.setRequestPayload(requestPayload);
            log.setResponsePayload(responsePayload);
            log.setTimestamp(LocalDateTime.now());

            coinRepository.save(log);



        }

}
