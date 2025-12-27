package com.project.InfluenceNet.schedulerReminderService.service;

import com.project.InfluenceNet.schedulerReminderService.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerReminderService.repository.PostingSchedulesRepository;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostingSchedulesService {

    private final PostingSchedulesRepository postingSchedulesRepository;

    public PostingScheduleDTO createReminder(UUID influencerId, Platform platform, Set<LocalTime> schedules){

        PostingSchedules postingSchedules = PostingSchedules.builder()
                .influencerId(influencerId)
                .platform(platform)
                .schedules(schedules)
                .reminderStatus(ReminderStatus.PENDING.name())
                .content("Time to Post!!")
                .created_at(new Timestamp(System.currentTimeMillis()))
                .updated_at(new Timestamp(System.currentTimeMillis()))
                .build();

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }

    public PostingScheduleDTO addSchedule(UUID influencerId, Platform platform, LocalTime schedule){

        PostingSchedules postingSchedules = postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform);

        if(postingSchedules == null){
            throw new RuntimeException("No Schedules Found");
        }

        Set<LocalTime> schedules = postingSchedules.getSchedules();
        schedules.add(schedule);
        postingSchedules.setSchedules(schedules);

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }

    public PostingScheduleDTO removeSchedule(UUID influencerId, Platform platform, LocalTime schedule){

        PostingSchedules postingSchedules = postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform);

        if(postingSchedules == null){
            throw new RuntimeException("No Schedules Found");
        }

        Set<LocalTime> schedules = postingSchedules.getSchedules();
        schedules.remove(schedule);
        postingSchedules.setSchedules(schedules);

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }

    public PostingScheduleDTO unpdateReminderStatus(UUID influencerId, Platform platform, ReminderStatus reminderStatus){

        PostingSchedules postingSchedules = postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform);

        if(postingSchedules == null){
            throw new RuntimeException("No Schedules Found");
        }

        postingSchedules.setReminderStatus(reminderStatus.name());

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }


    public void deletePostingSchedule(UUID influencerId, Platform platform){

        PostingSchedules postingSchedules = postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform);

        if(postingSchedules == null){
            throw new RuntimeException("No Schedules Found");
        }

        postingSchedulesRepository.delete(postingSchedules);

    }


}
