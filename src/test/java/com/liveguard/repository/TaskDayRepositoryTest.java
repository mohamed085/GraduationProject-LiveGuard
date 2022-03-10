package com.liveguard.repository;

import com.liveguard.domain.Currency;
import com.liveguard.domain.Day;
import com.liveguard.domain.TaskDay;
import com.liveguard.util.CaseTransfer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class TaskDayRepositoryTest {

    @Autowired
    private TaskDayRepository taskDayRepository;

    @Test
    void testSaveAll() {
        List<TaskDay> taskDays = Arrays.asList(
                new TaskDay(Day.Saturday),
                new TaskDay(Day.Sunday),
                new TaskDay(Day.Monday),
                new TaskDay(Day.Tuesday),
                new TaskDay(Day.Wednesday),
                new TaskDay(Day.Thursday),
                new TaskDay(Day.Friday)
        );

        taskDayRepository.saveAll(taskDays);

        Iterable<TaskDay> iterable = taskDayRepository.findAll();
        System.out.println(iterable);
        assertNotNull(iterable);
    }

    @Test
    void searchByDay() {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        String day = dayOfWeek.name();
        Day day1 = Day.valueOf(CaseTransfer.toLowerCaseExpectedFirstLetter(day));
        TaskDay taskDay = taskDayRepository.findByDay(day1);

        System.out.println(taskDay.toString());
        assertNotNull(taskDay);
    }
}