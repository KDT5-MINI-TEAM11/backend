package com.fastcampus.scheduling._core.batch;

import com.fastcampus.scheduling.schedule.model.Schedule;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
@Slf4j
@RequiredArgsConstructor
@Configuration
public class ScheduleCursorJobConfiguration {
	public static final String JOB_NAME = "ScheduleCursorJob";

	private final EntityManagerFactory entityManagerFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final DataSource dataSource;

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
				.writer(writer())
				.build();
	}

	@Bean
	@StepScope
	public JdbcCursorItemReader<Schedule> schedulePagingReader() {
		return new JdbcCursorItemReaderBuilder<Schedule>()
				.sql("SELECT * FROM schedule_tb s WHERE s.state = 'PENDING'")
				.rowMapper(new BeanPropertyRowMapper<>(Schedule.class))
				.fetchSize(CHUNK_SIZE)
				.dataSource(dataSource)
				.name("SchedulePagingReader")
				.build();
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
	public JpaItemWriter<Schedule> writer() {
		JpaItemWriter<Schedule> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}
}
