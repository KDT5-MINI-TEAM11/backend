package com.fastcampus.scheduling.admin.service;

import com.fastcampus.scheduling.admin.repository.AdminRepository;
import com.fastcampus.scheduling.schedule.dto.SchedulingResponse;
import com.fastcampus.scheduling.schedule.model.Schedule;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    public List<SchedulingResponse> find(){
        List<Schedule> scheduleList = adminRepository.findAll();
        List<SchedulingResponse> res = new ArrayList<>();
        for (Schedule schedule : scheduleList) {
            //res.add(schedule.getId())

        }
        return res;
    }
}
