# Windsurfers-Weather-Wervice

**Windsurfers-Weather-Service** is a Spring Boot application that provides windsurfing location recommendations based on weather conditions using the Weatherbit API.

## Project Overview
The Worldwide Windsurfer’s Weather Service application helps users find the best windsurfing locations based on specific weather conditions. It provides a REST API that takes a date (in **yyyy-mm-dd format**) as input and returns the best location for windsurfing out of the following predefined locations:

- Jastarnia (Poland)
- Bridgetown (Barbados)
- Fortaleza (Brazil)
- Pissouri (Cyprus)
- Le Morne (Mauritius)

## Features
Weather Data Source: Uses the Weatherbit Forecast API to fetch weather data.
REST API: Exposes a REST endpoint to fetch the best windsurfing location for a given day.
Best Location Selection Criteria:
The wind speed should be between 5 m/s and 18 m/s.
The temperature should be between 5°C and 35°C.
The best location is determined using the formula: v * 3 + temp where v is the wind speed in m/s and temp is the temperature in Celsius.

## Prerequisites
To run this application, you need to have:

- Java 8 or higher installed.
- Maven or Gradle installed.
- A valid Weatherbit API key. You can obtain an API key by signing up at the Weatherbit API.

## Setting Up the Application
Clone the Repository:

git clone https://github.com/Grzegorzboron23/Windsurfers-Weather-Service.git
cd Windsurfers-Weather-Service
Set the Weatherbit API Key:

Set up an environment variable WEATHERBIT_API_KEY2 with your Weatherbit API key. This key will be used to authenticate requests to the Weatherbit API.

Windows:

Open a Command Prompt and run:
``` set WEATHERBIT_API_KEY2=your_api_key_here ```
Linux/macOS:

Open a terminal and run:
``` export WEATHERBIT_API_KEY2=your_api_key_here ```
Update Application Configuration:

Ensure that your application.yml or application.properties file includes the following configuration:

yaml
```
server:
  port: 8080

weatherbit:
  api:
    key: ${WEATHERBIT_API_KEY2}
    base-url: https://api.weatherbit.io
```

Building and Running the Application
Using Maven
To build and run the application using Maven:

## Build the application:

### To run application use command prompt in your project folder: 
``` mvn spring-boot:run ```





## Troubleshooting
Ensure the Weatherbit API key is correctly set in your environment.
Make sure the Weatherbit API base URL and the key are correctly configured in the application.yml or application.properties file.
Check that your Java version is 8 or higher.

## License
This project is licensed under the MIT License.
