package com.afesguerra.sonarqube.plugin.sns;

import com.afesguerra.sonarqube.plugin.sns.sns.AmazonSNSClientProxy;
import com.afesguerra.sonarqube.plugin.sns.sns.SonarPropertiesAWSCredentialsProvider;
import org.sonar.api.Plugin;
import org.sonar.api.PropertyType;
import org.sonar.api.config.PropertyDefinition;

public class SNSNotificationPlugin implements Plugin {
    public static final String AWS_SNS_ENDPOINT_KEY = "sonar.aws.sns.endpoint";
    public static final String AWS_SNS_REGION_KEY = "sonar.aws.sns.region";
    public static final String AWS_SNS_TOPIC_ARN_KEY = "sonar.aws.sns.topicArn";
    public static final String AWS_SNS_ACCESS_KEY_CONFIG_KEY = "sonar.aws.sns.accessKey";
    public static final String AWS_SNS_SECRET_KEY_CONFIG_KEY = "sonar.aws.sns.secretKey";

    private static final String PROPERTIES_CATEGORY = "AWS SNS Notifications";

    private final PropertyDefinition awsSnsEndpoint = PropertyDefinition.builder(AWS_SNS_ENDPOINT_KEY)
            .name("AWS SNS endpoint")
            .description("Endpoint of the SNS Topic to publish messages to")
            .category(PROPERTIES_CATEGORY)
            .index(1)
            .build();

    private final PropertyDefinition awsRegion = PropertyDefinition.builder(AWS_SNS_REGION_KEY)
            .name("AWS Region")
            .description("AWS region of the SNS endpoint")
            .category(PROPERTIES_CATEGORY)
            .index(2)
            .build();

    private final PropertyDefinition awsSnsTopicArn = PropertyDefinition.builder(AWS_SNS_TOPIC_ARN_KEY)
            .name("AWS SNS Topic ARN")
            .description("ARN of the SNS Topic to publish messages to")
            .category(PROPERTIES_CATEGORY)
            .index(3)
            .build();

    private final PropertyDefinition awsAccessKey = PropertyDefinition.builder(AWS_SNS_ACCESS_KEY_CONFIG_KEY)
            .name("AWS Access Key")
            .description("Access key with permissions to publish to the SNS Topic")
            .category(PROPERTIES_CATEGORY)
            .index(4)
            .build();

    private final PropertyDefinition awsSecretKey = PropertyDefinition.builder(AWS_SNS_SECRET_KEY_CONFIG_KEY)
            .name("AWS Secret Key")
            .description("Secret key with permissions to publish to the SNS Topic")
            .category(PROPERTIES_CATEGORY)
            .type(PropertyType.PASSWORD)
            .index(5)
            .build();

    @Override
    public void define(Context context) {

        context.addExtensions(
                SonarPropertiesAWSCredentialsProvider.class,
                SNSNotificationTask.class,
                AmazonSNSClientProxy.class,
                awsSnsEndpoint,
                awsRegion,
                awsSnsTopicArn,
                awsAccessKey,
                awsSecretKey
        );
    }
}
