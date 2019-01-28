package com.afesguerra.sonarqube.plugin.sns;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.config.Settings;

import java.util.Objects;
import java.util.function.Supplier;

@Slf4j
@ComputeEngineSide
@ExtensionPoint
public class AmazonSNSClientProxy implements Supplier<AmazonSNS> {
    private final Settings settings;
    private final AWSCredentialsProvider awsCredentialsProvider;

    private AmazonSNS sns;

    public AmazonSNSClientProxy(Settings settings, SonarPropertiesAWSCredentialsProvider awsCredentialsProvider) {
        this.settings = settings;
        this.awsCredentialsProvider = awsCredentialsProvider;
    }

    @Override
    public AmazonSNS get() {
        if (sns == null) {
            final String awsRegion = settings.getString(SNSNotificationPluginConstants.AWS_SNS_REGION_KEY);

            log.debug("Creating SNS client");
            AmazonSNSClientBuilder amazonSNSClientBuilder = AmazonSNSClientBuilder
                    .standard()
                    .withCredentials(awsCredentialsProvider)
                    .withRegion(awsRegion);

            final String endpoint = settings.getString(SNSNotificationPluginConstants.AWS_SNS_ENDPOINT_KEY);
            if (Objects.nonNull(endpoint)) {
                final AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, awsRegion);
                amazonSNSClientBuilder = amazonSNSClientBuilder.withEndpointConfiguration(endpointConfiguration);
            }

            sns = amazonSNSClientBuilder.build();
        }

        return sns;
    }
}
