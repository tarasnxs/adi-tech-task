package com.epam.aditechtask.controller;

import com.epam.aditechtask.service.ExchangeRateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exchange-rate")
@Tag(name = "Exchange Rate", description = "API to retrieve latest exchange rate")
public class ExchangeRateController {

	private final ExchangeRateService exchangeRateService;

	public ExchangeRateController(ExchangeRateService exchangeRateService) {
		this.exchangeRateService = exchangeRateService;
	}

	@GetMapping("/{currency}")
	@Operation(summary = "Get exchange rate", description = "Retrieves latest exchange rate for provided currency")
	public BigDecimal getExchangeRate(@PathVariable String currency) {
		return exchangeRateService.getExchangeRate(currency);
	}
}