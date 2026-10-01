
package com.example.studentopportunitytracker.service;

import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.repository.ReminderRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReminderService {

    private final ReminderRepository reminderRepository;

    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }

    public Reminder createReminder(Reminder reminder) {
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
}
