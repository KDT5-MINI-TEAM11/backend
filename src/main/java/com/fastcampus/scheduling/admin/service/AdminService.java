package com.fastcampus.scheduling.admin.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminRequest.ApproveDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.RejectDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.admin.dto.AdminResponse.GetAllScheduleDTO;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final ScheduleRepository scheduleRepository;
    private final UserRepository userRepository;

    public List<AdminResponse.GetAllScheduleDTO> findAll(){
        List<Schedule> scheduleList = scheduleRepository.findAll();
        List<AdminResponse.GetAllScheduleDTO> getAllScheduleDTOList = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            getAllScheduleDTOList.add(GetAllScheduleDTO.builder()
                .id(schedule.getId())
                .userName(schedule.getUser().getUserName())
                .position(schedule.getUser().getPosition())
                .type(schedule.getScheduleType())
                .startDate(schedule.getStartDate())
                .endDate(schedule.getEndDate())
                .state(schedule.getState())
                .build());
        }
        return getAllScheduleDTOList;
    }

    @Transactional
    public AdminResponse.ResolveDTO updateScheduleReject(RejectDTO rejectDTO){
        Optional<Schedule> scheduleOptional = scheduleRepository.findByUserId(rejectDTO.getUserId());
        Optional<User> userOptional = userRepository.findById(rejectDTO.getUserId());

        if(!scheduleOptional.isPresent())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        Schedule schedule = scheduleRepository.save(Schedule.builder()
            .id(scheduleOptional.get().getId())
            .user(userOptional.get())
            .scheduleType(scheduleOptional.get().getScheduleType())
            .state(State.REJECT)
            .startDate(rejectDTO.getStartDate())
            .endDate(rejectDTO.getEndDate())
            .createdAt(scheduleOptional.get().getCreatedAt())
            .updatedAt(LocalDate.now())
            .build());

        return AdminResponse.ResolveDTO.builder()
            .scheduleType(schedule.getScheduleType())
            .state(schedule.getState())
            .build();
    }

    @Transactional
    public AdminResponse.ResolveDTO updateScheduleApprove(ApproveDTO approveDTO){
        Optional<Schedule> scheduleOptional = scheduleRepository.findByUserId(approveDTO.getUserId());
        Optional<User> userOptional = userRepository.findById(approveDTO.getUserId());

        if(!scheduleOptional.isPresent())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        Schedule schedule = scheduleRepository.save(Schedule.builder()
            .id(scheduleOptional.get().getId())
            .user(userOptional.get())
            .scheduleType(scheduleOptional.get().getScheduleType())
            .state(State.APPROVE)
            .startDate(approveDTO.getStartDate())
            .endDate(approveDTO.getEndDate())
            .createdAt(scheduleOptional.get().getCreatedAt())
            .updatedAt(LocalDate.now())
            .build());

        return AdminResponse.ResolveDTO.builder()
            .scheduleType(schedule.getScheduleType())
            .state(schedule.getState())
            .build();
    }

    @Transactional
    public void updatePosition(AdminRequest.UpdatePositionDTO updatePositionDTO){
        Optional<User> userOptional = userRepository.findById(updatePositionDTO.getId());

        if(!userOptional.isPresent())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SAVE_USER);

        if(userOptional.orElseThrow().getPosition() == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_POSITION);

        userRepository.save(User.builder()
            .id(userOptional.get().getId())
            .userEmail(userOptional.get().getUserEmail())
            .userPassword(userOptional.get().getUserPassword())
            .userName(userOptional.get().getUserName())
            .profileThumbUrl(userOptional.get().getProfileThumbUrl())
            .position(updatePositionDTO.getPosition())
            .phoneNumber(userOptional.get().getPhoneNumber())
            .usedVacation(userOptional.get().getUsedVacation())
            .createdAt(userOptional.get().getCreatedAt())
            .build());
    }
}
