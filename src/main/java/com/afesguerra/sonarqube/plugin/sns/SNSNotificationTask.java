package com.afesguerra.sonarqube.plugin.sns;

import com.afesguerra.sonarqube.plugin.sns.serializer.ConditionSerializer;
import com.afesguerra.sonarqube.plugin.sns.serializer.OptionalSerializer;
import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Settings;

import java.io.UncheckedIOException;
import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class SNSNotificationTask implements PostProjectAnalysisTask {
    private final Settings settings;
    private final Supplier<AmazonSNS> amazonSNSSupplier;

    public SNSNotificationTask(Settings settings, AmazonSNSClientProxy amazonSNSSupplier) {
        this.settings = settings;
        this.amazonSNSSupplier = amazonSNSSupplier;
    }

    @Override
    public void finished(ProjectAnalysis projectAnalysis) {
        final String msg = getNotificationMessage(projectAnalysis);
        final String topicArn = settings.getString(SNSNotificationPluginConstants.AWS_SNS_TOPIC_ARN_KEY);
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
