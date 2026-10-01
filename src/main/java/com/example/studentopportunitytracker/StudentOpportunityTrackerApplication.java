package com.example.studentopportunitytracker;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@EnableScheduling
@SpringBootApplication
public class StudentOpportunityTrackerApplication {

	public static void main(String[] args) {
		SpringApplication.run(StudentOpportunityTrackerApplication.class, args);
	}

}
