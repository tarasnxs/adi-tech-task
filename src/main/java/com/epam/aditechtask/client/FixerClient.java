package com.epam.aditechtask.client;

import com.epam.aditechtask.dto.ExchangeRateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@Slf4j
public class FixerClient {

	private final RestTemplate restTemplate;
	private final String apiUrl;
	private final String apiKey;

	public FixerClient(@Value("${fixer.api.url}") String apiUrl,
		@Value("${fixer.api.key}") String apiKey) {
		this.restTemplate = new RestTemplate();
		this.apiUrl = apiUrl;
		this.apiKey = apiKey;
	}

	public ExchangeRateResponse getExchangeRate(String currency) {
		log.info("Fetching exchange rate for currency {}", currency);
		String url = apiUrl + "?access_key=" + apiKey + "&symbols=" + currency;
		ResponseEntity<ExchangeRateResponse> response = restTemplate.getForEntity(url, ExchangeRateResponse.class);
		log.info("Exchange rate response: {}", response.getBody());
		return response.getBody();
	}
}
