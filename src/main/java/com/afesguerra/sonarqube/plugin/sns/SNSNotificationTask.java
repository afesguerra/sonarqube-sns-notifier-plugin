package com.afesguerra.sonarqube.plugin.sns;

import com.afesguerra.sonarqube.plugin.sns.serializer.ConditionSerializer;
import com.afesguerra.sonarqube.plugin.sns.serializer.OptionalSerializer;
import com.afesguerra.sonarqube.plugin.sns.sns.AmazonSNSClientProxy;
import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Configuration;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Supplier;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_TOPIC_ARN_KEY;

@Slf4j
public class SNSNotificationTask implements PostProjectAnalysisTask {
    private final Configuration configuration;
    private final Supplier<AmazonSNS> amazonSNSSupplier;

    public SNSNotificationTask(Configuration configuration, AmazonSNSClientProxy amazonSNSSupplier) {
        this.configuration = configuration;
        this.amazonSNSSupplier = amazonSNSSupplier;
    }

    @Override
    public void finished(ProjectAnalysis projectAnalysis) {
        final String topicArn = configuration.get(AWS_SNS_TOPIC_ARN_KEY)
                .orElseThrow(() -> {
                    final String message = String.format("Must define property %s", AWS_SNS_TOPIC_ARN_KEY);
                    return new RuntimeException(message);
                });

        final String msg = getNotificationMessage(projectAnalysis);
        final AmazonSNS sns = amazonSNSSupplier.get();

        log.info("Publishing message {}", msg);
        sns.publish(topicArn, msg);
    }

    private String getNotificationMessage(ProjectAnalysis projectAnalysis) {
        SimpleModule module = new SimpleModule();
        module.addSerializer(QualityGate.Condition.class, new ConditionSerializer());
        module.addSerializer(Optional.class, new OptionalSerializer());

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(module);

        try {
            return objectMapper.writeValueAsString(projectAnalysis);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
