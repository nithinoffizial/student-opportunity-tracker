package com.example.studentopportunitytracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.studentopportunitytracker.service.EmailService;

@RestController
@RequestMapping("/api/email")
public class EmailTestController {

    private final EmailService emailService;

    public EmailTestController(EmailService emailService) {
        this.emailService = emailService;
    }

    @GetMapping("/test")
    public String sendTestEmail(@RequestParam String to) {

        emailService.sendReminderEmail(
                to,
                "Java Full Stack Internship",
                "2026-10-20"
        );

        return "Test email sent successfully";
    }
}