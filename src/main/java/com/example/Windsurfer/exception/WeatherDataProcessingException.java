package com.example.Windsurfer.exception;

public class WeatherDataProcessingException extends Exception{
    public WeatherDataProcessingException(String message) {
        super(message);
    }

    public WeatherDataProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}
