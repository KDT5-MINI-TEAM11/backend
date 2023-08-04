package com.fastcampus.scheduling.admin.service;

import com.fastcampus.scheduling._core.errors.ErrorMessage;
import com.fastcampus.scheduling._core.errors.exception.Exception400;
import com.fastcampus.scheduling.admin.dto.AdminRequest;
import com.fastcampus.scheduling.admin.dto.AdminRequest.ApproveDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.PendingDTO;
import com.fastcampus.scheduling.admin.dto.AdminRequest.RejectDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse;
import com.fastcampus.scheduling.admin.dto.AdminResponse.GetAllScheduleDTO;
import com.fastcampus.scheduling.admin.dto.AdminResponse.GetAllUserDTO;
import com.fastcampus.scheduling.schedule.common.State;
import com.fastcampus.scheduling.schedule.model.Schedule;
import com.fastcampus.scheduling.schedule.repository.ScheduleRepository;
import com.fastcampus.scheduling.user.common.Position;
import com.fastcampus.scheduling.user.model.User;
import com.fastcampus.scheduling.user.repository.UserRepository;
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

    public List<AdminResponse.GetAllScheduleDTO> findAllSchedule(){
        List<Schedule> scheduleList = scheduleRepository.findAllByOrderByIdDesc();
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

    @Transactional(readOnly = true)
    public List<AdminResponse.GetAllUserDTO> findAllUser(){
        List<User> userList = userRepository.findAllByOrderByIdDesc();
        List<AdminResponse.GetAllUserDTO> getAllUserDTOList = new ArrayList<>();
        for (User user : userList) {
            getAllUserDTOList.add(GetAllUserDTO.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .profileThumbUrl(user.getProfileThumbUrl())
                .position(user.getPosition())
                .createAt(user.getCreatedAt())
                .build());
        }
        return getAllUserDTOList;
    }

    @Transactional
    public void updateScheduleReject(RejectDTO rejectDTO){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(rejectDTO.getId());

        scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
            .setState(State.REJECT);
    }

    @Transactional
    public void updateScheduleApprove(ApproveDTO approveDTO){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(approveDTO.getId());

        scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
            .setState(State.APPROVE);
    }

    @Transactional
    public void updateSchedulePending(PendingDTO pendingDTO){
        Optional<Schedule> scheduleOptional = scheduleRepository.findById(pendingDTO.getId());

        scheduleOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SCHEDULE))
            .setState(State.PENDING);
    }

    @Transactional
    public void updatePosition(AdminRequest.UpdatePositionDTO updatePositionDTO){
        Optional<User> userOptional = userRepository.findById(updatePositionDTO.getId());

        if(userOptional.orElseThrow(() -> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SAVE_USER))
            .getPosition() == null)
            throw new Exception400(ErrorMessage.EMPTY_DATA_FOR_USER_CHECK_POSITION);

        userOptional.orElseThrow(()-> new Exception400(ErrorMessage.EMPTY_DATA_FOR_SAVE_USER))
            .setPosition(updatePositionDTO.getPosition());
    }

    public Position getPosition(Long userId){
        Optional<User> userOptional = userRepository.findById(userId);

        return userOptional.orElseThrow().getPosition();
    }
}
