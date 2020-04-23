package com.afesguerra.sonarqube.plugin.sns.sns;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSCredentialsProviderChain;
import com.amazonaws.auth.EC2ContainerCredentialsProviderWrapper;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.auth.SystemPropertiesCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.config.Configuration;
import org.sonar.api.utils.log.Logger;
import org.sonar.api.utils.log.Loggers;

import java.util.function.Supplier;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_ENDPOINT_KEY;
import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_REGION_KEY;


@ComputeEngineSide
@ExtensionPoint
public class AmazonSNSClientProxy implements Supplier<AmazonSNS> {
    private static final Logger LOGGER = Loggers.get(AmazonSNSClientProxy.class);
    private final Configuration configuration;
    private final AWSCredentialsProvider awsCredentialsProvider;

    private AmazonSNS sns;

    public AmazonSNSClientProxy(Configuration configuration, SonarPropertiesAWSCredentialsProvider awsCredentialsProvider) {
        this.configuration = configuration;
        this.awsCredentialsProvider = new AWSCredentialsProviderChain(
                awsCredentialsProvider,
                new EnvironmentVariableCredentialsProvider(),
                new SystemPropertiesCredentialsProvider(),
                new ProfileCredentialsProvider(),
                new EC2ContainerCredentialsProviderWrapper()
        );
    }

    @Override
    public AmazonSNS get() {
        if (sns == null) {
            sns = createSNSClient();
        }

        return sns;
    }

    private AmazonSNS createSNSClient() {
        LOGGER.debug("Creating SNS client");

        final AmazonSNSClientBuilder snsBuilder = AmazonSNSClientBuilder.standard().withCredentials(awsCredentialsProvider);

        final String region = configuration.get(AWS_SNS_REGION_KEY).orElse(null);
        snsBuilder.setRegion(region);

        configuration.get(AWS_SNS_ENDPOINT_KEY).ifPresent(endpoint -> {
            final AwsClientBuilder.EndpointConfiguration endpointConfiguration = new AwsClientBuilder.EndpointConfiguration(endpoint, region);
            snsBuilder.withEndpointConfiguration(endpointConfiguration);
        });

        return snsBuilder.build();
    }
}
