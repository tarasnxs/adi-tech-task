package com.epam.aditechtask.service;

import com.epam.aditechtask.client.FixerClient;
import com.epam.aditechtask.dto.ExchangeRateResponse;
import java.math.BigDecimal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ExchangeRateService {

	private final FixerClient fixerClient;

	public ExchangeRateService(FixerClient fixerClient) {
		this.fixerClient = fixerClient;
	}

	@Cacheable(value = "exchangeRates", key = "#currency")
	public BigDecimal getExchangeRate(String currency) {
		ExchangeRateResponse response = fixerClient.getExchangeRate(currency);
		return response.rates().getOrDefault(currency, BigDecimal.ZERO);
	}
}