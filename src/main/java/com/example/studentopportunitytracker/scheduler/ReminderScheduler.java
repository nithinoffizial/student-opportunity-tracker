package com.example.studentopportunitytracker.scheduler;

import com.example.studentopportunitytracker.entity.Application;
import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.service.EmailService;
import com.example.studentopportunitytracker.service.ReminderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReminderScheduler {

    private static final Logger logger = LoggerFactory.getLogger(ReminderScheduler.class);

    private final ReminderService reminderService;
    private final EmailService emailService;

    public ReminderScheduler(ReminderService reminderService, EmailService emailService) {
        this.reminderService = reminderService;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 9 * * *")
    public void sendDailyReminders() {
        List<Reminder> reminders = reminderService.getTodayReminders();
        logger.info("Found {} reminders to send for today", reminders.size());

        for (Reminder reminder : reminders) {
            try {
                Application application = reminder.getApplication();
                if (application == null || application.getStudent() == null || application.getOpportunity() == null) {
                    logger.warn("Skipping reminder id {}: incomplete application/student/opportunity reference", reminder.getId());
                    continue;
                }

                String email = application.getStudent().getEmail();
                if (email == null || email.isBlank()) {
                    logger.warn("Skipping reminder id {}: missing student email", reminder.getId());
                    continue;
                }

                String opportunityTitle = application.getOpportunity().getTitle() != null
                        ? application.getOpportunity().getTitle()
                        : "Opportunity";

                String studentName = application.getStudent().getName() != null
                        ? application.getStudent().getName()
                        : "Student";

                String company = application.getOpportunity().getCompany() != null
                        ? application.getOpportunity().getCompany()
                        : "Company";

                String deadline = application.getOpportunity().getDeadline() != null
                        ? application.getOpportunity().getDeadline().toString()
                        : "N/A";

                String subject = "Opportunity Reminder: " + opportunityTitle;

                String message = "Hello " + studentName + ",\n\n"
                        + "This is a reminder about your opportunity:\n\n"
                        + opportunityTitle + "\n"
                        + "Company: " + company + "\n"
                        + "Deadline: " + deadline + "\n\n"
                        + "Please check the opportunity and take the necessary action.\n\n"
                        + "Student Opportunity Tracker";

                emailService.sendReminderEmail(email, subject, message);
                reminderService.markAsSent(reminder);
                logger.info("Reminder id {} sent successfully to {}", reminder.getId(), email);
            } catch (Exception ex) {
                logger.error("Failed to send reminder id {}: {}", reminder.getId(), ex.getMessage());
            }
        }
    }
}