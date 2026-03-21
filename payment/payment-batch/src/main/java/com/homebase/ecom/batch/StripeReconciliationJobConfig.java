package com.homebase.ecom.batch;

import com.homebase.ecom.batch.dto.StripeSettlementRow;
import com.homebase.ecom.payment.infrastructure.persistence.entity.ReconciliationMismatch;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.file.FlatFileItemReader;
import org.springframework.batch.infrastructure.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.infrastructure.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class StripeReconciliationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final StripeSettlementItemProcessor itemProcessor;
    private final StripeSettlementItemWriter itemWriter;

    public StripeReconciliationJobConfig(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StripeSettlementItemProcessor itemProcessor,
            StripeSettlementItemWriter itemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
    }

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public FlatFileItemReader<StripeSettlementRow> stripeCsvItemReader(
            @org.springframework.beans.factory.annotation.Value("#{jobParameters['filePath']}") String filePath) {

        String path = filePath != null ? filePath : "stripe-settlement-latest.csv";

        return new FlatFileItemReaderBuilder<StripeSettlementRow>()
                .name("stripeCsvItemReader")
                .resource(new FileSystemResource(path))
                .delimited()
                // Assume CSV headers matches these names somewhat
                .names("chargeId", "amount", "fee", "net", "currency", "status")
                .linesToSkip(1) // Skip headers
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(StripeSettlementRow.class);
                    }
                })
                .build();
    }

    @Bean
    public Step processStripeSettlementStep(FlatFileItemReader<StripeSettlementRow> stripeCsvItemReader) {
        return new StepBuilder("processStripeSettlementStep", jobRepository)
                .<StripeSettlementRow, ReconciliationMismatch>chunk(100, transactionManager)
                .reader(stripeCsvItemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job stripeReconciliationJob(Step processStripeSettlementStep) {
        return new JobBuilder("stripeReconciliationJob", jobRepository)
                .start(processStripeSettlementStep)
                .build();
    }
}
