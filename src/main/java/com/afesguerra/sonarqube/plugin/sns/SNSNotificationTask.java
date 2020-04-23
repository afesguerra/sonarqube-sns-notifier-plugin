package com.afesguerra.sonarqube.plugin.sns;

import com.afesguerra.sonarqube.plugin.sns.serializer.ConditionSerializer;
import com.afesguerra.sonarqube.plugin.sns.sns.AmazonSNSClientProxy;
import com.amazonaws.services.sns.AmazonSNS;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import org.sonar.api.ce.posttask.PostProjectAnalysisTask;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.io.UncheckedIOException;
import java.util.function.Supplier;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_TOPIC_ARN_KEY;

public class SNSNotificationTask implements PostProjectAnalysisTask {
    private static final Logger LOGGER = Loggers.get(SNSNotificationTask.class);

    private static final ObjectMapper OBJECT_MAPPER;

    private final Configuration configuration;
    private final Supplier<AmazonSNS> amazonSNSSupplier;

    static {
        SimpleModule module = new SimpleModule();
        module.addSerializer(QualityGate.Condition.class, new ConditionSerializer());
        OBJECT_MAPPER = new ObjectMapper()
                .registerModule(module)
                .registerModule(new Jdk8Module());
    }

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

        LOGGER.info("Publishing message {}", msg);
        sns.publish(topicArn, msg);
    }

    private String getNotificationMessage(ProjectAnalysis projectAnalysis) {
        try {
            return OBJECT_MAPPER.writeValueAsString(projectAnalysis);
        } catch (JsonProcessingException e) {
            throw new UncheckedIOException(e);
        }
    }
}
