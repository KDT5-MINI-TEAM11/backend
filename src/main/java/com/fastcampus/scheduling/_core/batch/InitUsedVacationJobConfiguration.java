package com.fastcampus.scheduling._core.batch;

import com.fastcampus.scheduling.user.model.User;
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
public class InitUsedVacationJobConfiguration {
	public static final String JOB_NAME = "InitUsedVacation";

	private final EntityManagerFactory entityManagerFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final JobBuilderFactory jobBuilderFactory;
	private final int CHUNK_SIZE = 100;

	@Bean
	public Job initUsedVacationPagingJob() {
		return jobBuilderFactory.get(JOB_NAME)
				.start(initUsedVacationPagingStep())
				.build();
	}

	@Bean
	@JobScope
	public Step initUsedVacationPagingStep() {
		return stepBuilderFactory.get("InitUsedVacationStep")
				.<User, User>chunk(CHUNK_SIZE)
				.reader(initUsedVacationPagingReader())
				.processor(initUsedVacationPagingProcessor())
				.writer(initUsedVacationWriter())
				.build();
	}

	@Bean
	@StepScope
	public JpaPagingItemReader<User> initUsedVacationPagingReader() {
		JpaPagingItemReader<User> reader = new JpaPagingItemReader<User>() {
			@Override
			public int getPage() {
				return 0;
			}
		};

		reader.setQueryString("SELECT u FROM User u");
		reader.setPageSize(CHUNK_SIZE);
		reader.setEntityManagerFactory(entityManagerFactory);
		reader.setName("InitUsedVacationPagingReader");

		return reader;
	}

	@Bean
	@StepScope
	public ItemProcessor<User, User> initUsedVacationPagingProcessor() {
		return item -> {
			item.initUsedVacation();
			return item;
		};
	}

	@Bean
	@StepScope
	public JpaItemWriter<User> initUsedVacationWriter() {
		JpaItemWriter<User> writer = new JpaItemWriter<>();
		writer.setEntityManagerFactory(entityManagerFactory);
		return writer;
	}

}
