package com.homebase.ecom.catalog.scheduler.config;

import com.homebase.ecom.catalog.model.CatalogItem;
import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CatalogItemRepository;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.support.ListItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.List;

@Configuration
public class BatchConfig {

    @Bean
    public ItemReader<CatalogItem> catalogItemReader(CatalogItemRepository catalogItemRepository) {
        return new ListItemReader<>(catalogItemRepository.findAll());
    }

    @Bean
    public Job refreshCollectionsJob(JobRepository jobRepository, Step evaluationStep) {
        return new JobBuilder("refreshCollectionsJob", jobRepository)
                .start(evaluationStep)
                .build();
    }

    @Bean
    public Step evaluationStep(JobRepository jobRepository,
                               PlatformTransactionManager transactionManager,
                               ItemReader<CatalogItem> reader,
                               ItemProcessor<CatalogItem, List<CollectionProductMapping>> processor,
                               ItemWriter<List<CollectionProductMapping>> writer) {
        return new StepBuilder("evaluateRulesStep", jobRepository)
                .<CatalogItem, List<CollectionProductMapping>>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
