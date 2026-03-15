package com.project.InfluenceNet.schedulerreminderservice.service;

import com.project.InfluenceNet.schedulerreminderservice.dto.PostingScheduleDTO;
import com.project.InfluenceNet.schedulerreminderservice.entity.FrequencyType;
import com.project.InfluenceNet.schedulerreminderservice.entity.PostingSchedules;
import com.project.InfluenceNet.schedulerreminderservice.entity.ReminderStatus;
import com.project.InfluenceNet.schedulerreminderservice.exception.DuplicatePostingScheduleException;
import com.project.InfluenceNet.schedulerreminderservice.exception.PostingScheduleNotFoundException;
import com.project.InfluenceNet.schedulerreminderservice.repository.PostingSchedulesRepository;
import com.project.InfluenceNet.schedulerreminderservice.entity.Platform;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostingSchedulesService {

    private final PostingSchedulesRepository postingSchedulesRepository;

    @Transactional
    public PostingScheduleDTO createReminder(UUID influencerId, String recipientEmail, Platform platform, FrequencyType frequencyType, Integer frequencyValue, LocalTime startTime){

        if (postingSchedulesRepository
                .existsByInfluencerIdAndPlatform(influencerId, platform)) {

            throw new DuplicatePostingScheduleException(
                    "Schedule already exists"
            );
        }

        PostingSchedules postingSchedules = PostingSchedules.builder()
                .influencerId(influencerId)
                .recipientEmail(recipientEmail)
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
        return PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);

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
        return PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);
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
        return PostingScheduleDTO.mapToPostingScheduleDTO(savedPostingSchedules);

    }

}
