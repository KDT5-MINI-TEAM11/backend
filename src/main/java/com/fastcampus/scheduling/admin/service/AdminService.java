package com.fastcampus.scheduling.admin.service;

import com.fastcampus.scheduling._core.common.Constants;
import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminRequest.ApproveDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.PendingDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.RejectDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
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

    @Transactional(readOnly = true)
    public List<AdminResponse.GetAllScheduleDTO> findAllSchedule() {
        List<Schedule> scheduleList = scheduleRepository.findAllByOrderByIdDesc();

        if(scheduleList == null || scheduleList.isEmpty())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        return scheduleList.stream()
            .map(schedule -> AdminResponse.GetAllScheduleDTO.from(schedule))
            .toList();
    }

    @Transactional(readOnly = true)
    public List<AdminResponse.GetAllUserDTO> findAllUser() {
        List<User> userList = userRepository.findAllByOrderByIdDesc();

        if(userList == null || userList.isEmpty())
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SAVE_USER);

        return userList.stream()
            .map(user -> AdminResponse.GetAllUserDTO.from(user))
            .toList();
    }

    @Transactional
    public String updateScheduleReject(RejectDTO rejectDTO) {
        if(rejectDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        try {
            Optional<Schedule> scheduleOptional = scheduleRepository.findById(rejectDTO.getId());
            scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
                .setState(State.REJECT);
        }catch (IllegalArgumentException e){
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);
        }

        return Constants.SCHEDULE_REJECT;
    }

    @Transactional
    public String updateScheduleApprove(ApproveDTO approveDTO) {
        if(approveDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        try {
            Optional<Schedule> scheduleOptional = scheduleRepository.findById(approveDTO.getId());

            scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
                .setState(State.APPROVE);
        }catch (IllegalArgumentException e){
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);
        }

        return Constants.SCHEDULE_APPROVE;
    }

    @Transactional
    public void updateSchedulePending(PendingDTO pendingDTO) {
        if(pendingDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);

        try {
            Optional<Schedule> scheduleOptional = scheduleRepository.findById(pendingDTO.getId());

            scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
                .setState(State.PENDING);
        }catch (IllegalArgumentException e){
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);
        }
    }

    @Transactional
    public void updatePosition(AdminRequest.UpdatePositionDTO updatePositionDTO) {
        if(updatePositionDTO == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_CHECK_POSITION);

        try {
            User user = userRepository.findById(updatePositionDTO.getId())
                .orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SAVE_USER));

            user.setPosition(updatePositionDTO.getPosition());
        }catch (IllegalArgumentException e){
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE);
        }
    }
}
