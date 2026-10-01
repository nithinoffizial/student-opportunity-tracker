package com.example.studentopportunitytracker.repository;

import com.example.studentopportunitytracker.entity.Reminder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    List<Reminder> findByReminderDateAndSentFalse(LocalDate reminderDate);
}