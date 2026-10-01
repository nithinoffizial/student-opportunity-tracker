package com.example.studentopportunitytracker.controller;

import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.service.ReminderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
public class ReminderController {

    private final ReminderService reminderService;

    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }

    @PostMapping
    public Reminder createReminder(@RequestBody Reminder reminder) {
        return reminderService.createReminder(reminder);
    }

    @GetMapping
    public List<Reminder> getAllReminders() {
        return reminderService.getAllReminders();
    }

    @GetMapping("/today")
    public List<Reminder> getTodayReminders() {
        return reminderService.getTodayReminders();
    }
}