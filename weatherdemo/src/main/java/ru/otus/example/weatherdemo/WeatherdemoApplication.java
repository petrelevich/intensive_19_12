package ru.otus.example.weatherdemo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import ru.otus.example.weatherdemo.services.WeatherAggregationService;

/*
О формате логов
http://openjdk.java.net/jeps/158


-Xms512m
-Xmx512m
-Xlog:gc=debug:file=./logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
-XX:+HeapDumpOnOutOfMemoryError
-XX:HeapDumpPath=./logs/dump
-XX:+UseG1GC
-XX:MaxGCPauseMillis=10
*/


@SpringBootApplication
public class WeatherdemoApplication {
	private static Logger logger = LoggerFactory.getLogger(WeatherAggregationService.class);

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

	public static void main(String[] args) {
		logger.info("starting app...");
		SpringApplication.run(WeatherdemoApplication.class, args);
	}

}
