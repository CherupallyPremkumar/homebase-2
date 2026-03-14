package com.homebase.ecom.batch;

import com.homebase.ecom.batch.dto.RazorpaySettlementRow;
import com.homebase.ecom.payment.domain.ReconciliationMismatch;
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
public class RazorpayReconciliationJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final RazorpaySettlementItemProcessor itemProcessor;
    private final RazorpaySettlementItemWriter itemWriter;

    public RazorpayReconciliationJobConfig(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            RazorpaySettlementItemProcessor itemProcessor,
            RazorpaySettlementItemWriter itemWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.itemProcessor = itemProcessor;
        this.itemWriter = itemWriter;
    }

    @Bean
    @org.springframework.batch.core.configuration.annotation.StepScope
    public FlatFileItemReader<RazorpaySettlementRow> razorpayCsvItemReader(
            @org.springframework.beans.factory.annotation.Value("#{jobParameters['filePath']}") String filePath) {

        String path = filePath != null ? filePath : "razorpay-settlement-latest.csv";

        return new FlatFileItemReaderBuilder<RazorpaySettlementRow>()
                .name("razorpayCsvItemReader")
                .resource(new FileSystemResource(path))
                .delimited()
                .names("paymentId", "amount", "fee", "net", "currency", "status")
                .linesToSkip(1)
                .fieldSetMapper(new BeanWrapperFieldSetMapper<>() {
                    {
                        setTargetType(RazorpaySettlementRow.class);
                    }
                })
                .build();
    }

    @Bean
    public Step processRazorpaySettlementStep(FlatFileItemReader<RazorpaySettlementRow> razorpayCsvItemReader) {
        return new StepBuilder("processRazorpaySettlementStep", jobRepository)
                .<RazorpaySettlementRow, ReconciliationMismatch>chunk(100, transactionManager)
                .reader(razorpayCsvItemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    public Job razorpayReconciliationJob(Step processRazorpaySettlementStep) {
        return new JobBuilder("razorpayReconciliationJob", jobRepository)
                .start(processRazorpaySettlementStep)
                .build();
    }
}
