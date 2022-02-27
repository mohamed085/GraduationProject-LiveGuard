package com.liveguard.service.serviceImp;

import com.liveguard.domain.*;
import com.liveguard.dto.LocationDTO;
import com.liveguard.mapper.LocationMapper;
import com.liveguard.repository.LocationRepository;
import com.liveguard.repository.TaskDayRepository;
import com.liveguard.repository.TaskRepository;
import com.liveguard.service.ChipService;
import com.liveguard.service.LocationService;
import com.liveguard.util.CaseTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class LocationServiceImp implements LocationService {

    private final LocationRepository locationRepository;
    private final ChipService chipService;
    private final TaskDayRepository taskDayRepository;
    private final TaskRepository taskRepository;

    public LocationServiceImp(LocationRepository locationRepository, ChipService chipService,
                              TaskDayRepository taskDayRepository, TaskRepository taskRepository) {
        this.locationRepository = locationRepository;
        this.chipService = chipService;
        this.taskDayRepository = taskDayRepository;
        this.taskRepository = taskRepository;
    }

    @Override
    public Location add(LocationDTO locationDTO) {
        log.debug("chipService | add | chip id: " + locationDTO.getChipId());
        Chip chip = chipService.findById(locationDTO.getChipId());
        Location location = LocationMapper.locationDTOToLocation(locationDTO);
        location.setDate(LocalDateTime.now());
        location.setChip(chip);
        Location savedLocation = locationRepository.save(location);

        pushLocationToSpecificChipChannel(savedLocation);
        List<Task> currentTasks = getCurrentTasks(savedLocation);
        currentTasks.forEach(task -> checkLocationStatus(task, savedLocation));

        /**
         * TODO
         * Send in socket
         * Get Current Task
         * Check location status
         * Push Notification if status out
         */

        return savedLocation;
    }

    private void pushLocationToSpecificChipChannel(Location location) {
        log.debug("LocationService | add | pushLocationToSpecificChipChannel");
        log.debug("LocationService | add | pushLocationToSpecificChipChannel | Push location in chip id: " + location.getChip().getId());
    }

    private List<Task> getCurrentTasks(Location location) {
        log.debug("LocationService | add | getCurrentTasks");
        DayOfWeek dayOfWeek = location.getDate().getDayOfWeek();
        String day = dayOfWeek.name();
        log.debug("LocationService | add | getCurrentTasks | current day: " + day);

        Day day1 = Day.valueOf(CaseTransfer.toLowerCaseExpectedFirstLetter(day));
        TaskDay taskDay = taskDayRepository.findByDay(day1);
        log.debug("LocationService | add | getCurrentTasks | current day id: " + taskDay.getId());

        LocalTime time = location.getDate().toLocalTime();
        log.debug("LocationService | add | getCurrentTasks | current time: " + time);

        List<Task> tasks = taskRepository
                .findByChipIdAndRepeatEquals(location.getChip().getId(), taskDay)
                .stream()
                .filter(task -> task.getStartDate().isBefore(time) && task.getEndDate().isAfter(time))
                .collect(Collectors.toList());

        log.debug("LocationService | add | getCurrentTasks | current tasks: " + tasks);
        return tasks;
    }

    private void checkLocationStatus(Task task, Location location) {
        log.debug("LocationService | add | checkLocationStatus");
        log.debug("LocationService | add | checkLocationStatus | task id: " + task.getId());
        log.debug("LocationService | add | checkLocationStatus | location id: " + location.getId());


    }
}
