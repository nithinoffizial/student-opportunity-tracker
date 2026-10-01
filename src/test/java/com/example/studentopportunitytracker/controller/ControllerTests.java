package com.example.studentopportunitytracker.controller;

import com.example.studentopportunitytracker.entity.Application;
import com.example.studentopportunitytracker.entity.Opportunity;
import com.example.studentopportunitytracker.entity.Reminder;
import com.example.studentopportunitytracker.entity.Student;
import com.example.studentopportunitytracker.service.ApplicationService;
import com.example.studentopportunitytracker.service.OpportunityService;
import com.example.studentopportunitytracker.service.ReminderService;
import com.example.studentopportunitytracker.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ControllerTests {

    @Mock
    private StudentService studentService;

    @Mock
    private OpportunityService opportunityService;

    @Mock
    private ApplicationService applicationService;

    @Mock
    private ReminderService reminderService;

    private StudentController studentController;
    private OpportunityController opportunityController;
    private ApplicationController applicationController;
    private ReminderController reminderController;

    @BeforeEach
    void setUp() {
        studentController = new StudentController(studentService);
        opportunityController = new OpportunityController(opportunityService);
        applicationController = new ApplicationController(applicationService);
        reminderController = new ReminderController(reminderService);
    }

    @Test
    void testCreateStudent() {
        Student input = new Student();
        input.setName("Alice");
        input.setEmail("alice@example.com");
        input.setCollege("Stanford");

        when(studentService.createStudent(input)).thenReturn(input);

        Student result = studentController.createStudent(input);
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(studentService).createStudent(input);
    }

    @Test
    void testGetAllStudents() {
        Student s = new Student();
        s.setName("Bob");
        when(studentService.getAllStudents()).thenReturn(List.of(s));

        List<Student> results = studentController.getAllStudents();
        assertEquals(1, results.size());
        assertEquals("Bob", results.get(0).getName());
    }

    @Test
    void testDeleteStudent() {
        doNothing().when(studentService).deleteStudent(1L);

        studentController.deleteStudent(1L);
        verify(studentService).deleteStudent(1L);
    }

    @Test
    void testCreateApplication() {
        Application app = new Application();
        app.setStatus("INTERESTED");
        app.setAppliedDate(LocalDate.now());

        when(applicationService.createApplication(app)).thenReturn(app);

        Application result = applicationController.createApplication(app);
        assertNotNull(result);
        assertEquals("INTERESTED", result.getStatus());
        verify(applicationService).createApplication(app);
    }

    @Test
    void testDeleteApplication() {
        doNothing().when(applicationService).deleteApplication(5L);

        applicationController.deleteApplication(5L);
        verify(applicationService).deleteApplication(5L);
    }

    @Test
    void testCreateReminder() {
        Reminder rem = new Reminder();
        rem.setReminderDate(LocalDate.now());

        when(reminderService.createReminder(rem)).thenReturn(rem);

        Reminder result = reminderController.createReminder(rem);
        assertNotNull(result);
        verify(reminderService).createReminder(rem);
    }

    @Test
    void testDeleteReminder() {
        doNothing().when(reminderService).deleteReminder(3L);

        reminderController.deleteReminder(3L);
        verify(reminderService).deleteReminder(3L);
    }

    @Test
    void testUpdateApplication() {
        Application updated = new Application();
        updated.setStatus("OFFERED");

        when(applicationService.updateApplication(eq(10L), any(Application.class))).thenReturn(updated);

        Application result = applicationController.updateApplication(10L, updated);
        assertNotNull(result);
        assertEquals("OFFERED", result.getStatus());
        verify(applicationService).updateApplication(10L, updated);
    }

    @Test
    void testMarkReminderAsSent() {
        Reminder rem = new Reminder();
        rem.setSent(true);

        when(reminderService.markReminderAsSent(4L)).thenReturn(rem);

        Reminder result = reminderController.markAsSent(4L);
        assertNotNull(result);
        assertTrue(result.isSent());
        verify(reminderService).markReminderAsSent(4L);
    }
}
