package com.homebase.ecom.catalog.scheduler.config;

import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.product.dto.ProductCatalogDetails;

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

    // Simple in-memory reader for demo. Real world: Use ItemReader<ProductCatalogDetails> with pagination
    @Bean
    public ItemReader<ProductCatalogDetails> productItemReader(com.homebase.ecom.catalog.repository.ProductServiceClient client) {
        return new ListItemReader<>(client.getAllProducts());
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
                               ItemReader<ProductCatalogDetails> reader,
                               ItemProcessor<ProductCatalogDetails, List<CollectionProductMapping>> processor,
                               ItemWriter<List<CollectionProductMapping>> writer) {
        return new StepBuilder("evaluateRulesStep", jobRepository)
                .<ProductCatalogDetails, List<CollectionProductMapping>>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
