package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Application;
import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.exception.ResourceNotFoundException;
import com.example.studentopportunitytracker.repository.ApplicationRepository;
import com.example.studentopportunitytracker.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final ApplicationRepository applicationRepository;

    public ReminderService(ReminderRepository reminderRepository,
                           ApplicationRepository applicationRepository) {
        this.reminderRepository = reminderRepository;
        this.applicationRepository = applicationRepository;
    }

    public Reminder createReminder(Reminder reminder) {
        if (reminder.getApplication() == null || reminder.getApplication().getId() == null) {
            throw new IllegalArgumentException("Application ID is required");
        }
        if (reminder.getReminderDate() == null) {
            throw new IllegalArgumentException("Reminder date is required");
        }

        Application application = applicationRepository.findById(reminder.getApplication().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with id: " + reminder.getApplication().getId()));

        reminder.setApplication(application);
        return reminderRepository.save(reminder);
    }

    public List<Reminder> getAllReminders() {
        return reminderRepository.findAll();
    }

    public List<Reminder> getTodayReminders() {
        return reminderRepository.findByReminderDateAndSentFalse(LocalDate.now());
    }

    public void markAsSent(Reminder reminder) {
        reminder.setSent(true);
        reminderRepository.save(reminder);
    }

    public Reminder markReminderAsSent(Long id) {
        Reminder reminder = reminderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reminder not found with id: " + id));
        reminder.setSent(true);
        return reminderRepository.save(reminder);
    }

    public void deleteReminder(Long id) {
        if (!reminderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reminder not found with id: " + id);
        }
        reminderRepository.deleteById(id);
    }
}
