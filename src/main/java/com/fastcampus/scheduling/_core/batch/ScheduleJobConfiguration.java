package com.fastcampus.scheduling._core.batch;

import com.fastcampus.scheduling.schedule.model.Schedule;
import javax.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScheduleJobConfiguration {
	public static final String JOB_NAME = "ScheduleCursorJob";

	private final EntityManagerFactory entityManagerFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final int CHUNK_SIZE = 100;

	@Bean
	public Job schedulePagingJob() {
		return jobBuilderFactory.get(JOB_NAME)
				.start(schedulePagingStep())
				.build();
	}

	@Bean
	@JobScope
	public Step schedulePagingStep() {
		return stepBuilderFactory.get("SchedulePagingStep")
				.<Schedule, Schedule>chunk(CHUNK_SIZE)
				.reader(schedulePagingReader())
				.processor(schedulePagingProcessor())
				.writer(scheduleWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<Schedule> schedulePagingReader() {
		JpaPagingItemReader<Schedule> reader = new JpaPagingItemReader<Schedule>() {
			@Override
			public int getPage() {
				return 0;
			}
		};

		reader.setQueryString("SELECT s FROM Schedule s WHERE s.state = 'PENDING'");
		reader.setPageSize(CHUNK_SIZE);
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setName("SchedulePagingReader");

		return reader;
	}

	@Bean
	@StepScope
	public ItemProcessor<Schedule, Schedule> schedulePagingProcessor() {
		return item -> {
			item.rejectScheduleOverDate();
			return item;
		};
	}

	@Bean
	@StepScope
	public JpaItemWriter<Schedule> scheduleWriter() {
		JpaItemWriter<Schedule> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

}
