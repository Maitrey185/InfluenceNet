package com.project.InfluenceNet.schedulerReminderService.service;

import com.project.InfluenceNet.schedulerReminderService.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerReminderService.entity.FrequencyType;
import com.project.InfluenceNet.schedulerReminderService.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerReminderService.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerReminderService.exception.DuplicatePostingScheduleException;
import com.project.InfluenceNet.schedulerReminderService.exception.PostingScheduleNotFoundException;
import com.project.InfluenceNet.schedulerReminderService.repository.PostingSchedulesRepository;
import com.project.InfluenceNet.socialConnector.documents.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostingSchedulesService {

    private final PostingSchedulesRepository postingSchedulesRepository;

    @Transactional
    public PostingScheduleDTO createReminder(UUID influencerId, Platform platform, FrequencyType frequencyType, Integer frequencyValue, LocalTime startTime){


        if (postingSchedulesRepository
                .existsByInfluencerIdAndPlatform(influencerId, platform)) {

            throw new DuplicatePostingScheduleException(
                    "Schedule already exists"
            );
        }

        PostingSchedules postingSchedules = PostingSchedules.builder()
                .influencerId(influencerId)
                .platform(platform)
                .reminderStatus(ReminderStatus.PENDING)
                .content("Time to Post!!")
                .frequencyType(frequencyType)
                .frequencyValue(frequencyValue)
                .startTime(startTime)
                .created_at(new Timestamp(System.currentTimeMillis()))
                .updated_at(new Timestamp(System.currentTimeMillis()))
                .build();

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.saveAndFlush(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }

    public PostingScheduleDTO updatePostingFrequency(UUID influencerId, Platform platform, FrequencyType frequencyType, Integer frequencyValue, LocalTime startTime){

        PostingSchedules postingSchedules =
                Optional.ofNullable(
                        postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                ).orElseThrow(() ->
                        new PostingScheduleNotFoundException("No posting schedule found")
                );

        postingSchedules.setFrequencyType(frequencyType);
        postingSchedules.setFrequencyValue(frequencyValue);
        postingSchedules.setStartTime(startTime);
        postingSchedules.setUpdated_at(new Timestamp(System.currentTimeMillis()));

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;
    }



    public PostingScheduleDTO unpdateReminderStatus(UUID influencerId, Platform platform, ReminderStatus reminderStatus){

        PostingSchedules postingSchedules =
                Optional.ofNullable(
                        postingSchedulesRepository.findByInfluencerIdAndPlatform(influencerId, platform)
                ).orElseThrow(() ->
                        new PostingScheduleNotFoundException("No posting schedule found")
                );

        postingSchedules.setReminderStatus(reminderStatus);

        PostingSchedules savedPostingSchedules = postingSchedulesRepository.save(postingSchedules);
        PostingScheduleDTO postingScheduleDTO = PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
        return postingScheduleDTO;

    }





}
