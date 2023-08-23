package com.fastcampus.scheduling._core.batch;

import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DayChangeScheduler {

	private final ScheduleJobConfiguration scheduleJobConfiguration;
	private final InitUsedVacationJobConfiguration initUsedVacationJobConfiguration;
	private final JobLauncher jobLauncher;

	@Scheduled(cron = "0 0 0 * * *")
	public void scheduleStateChangeRun() {
		// job parameter 설정
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(confMap);

		try {
			jobLauncher.run(scheduleJobConfiguration.schedulePagingJob(), jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
						 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

			log.error(e.getMessage());
		}
	}

	@Scheduled(cron = "0 0 0 1 1 ?")
	public void initUsedVacationRun() {
		// job parameter 설정
		Map<String, JobParameter> confMap = new HashMap<>();
		confMap.put("time", new JobParameter(System.currentTimeMillis()));
		JobParameters jobParameters = new JobParameters(confMap);

		try {
			jobLauncher.run(initUsedVacationJobConfiguration.initUsedVacationPagingJob(), jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
						 | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {

			log.error(e.getMessage());
		}
	}

}
