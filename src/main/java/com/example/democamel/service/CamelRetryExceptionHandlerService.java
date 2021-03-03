package com.example.democamel.service;

import org.apache.camel.Exchange;
import org.apache.camel.ExchangeException;

public interface CamelRetryExceptionHandlerService {
    void handleCustomException(Exchange e, @ExchangeException Exception causedBy);
}
