package com.afesguerra.sonarqube.plugin.sns;

public final class SNSNotificationPluginConstants {
    private SNSNotificationPluginConstants() {}

    public static final String AWS_SNS_REGION_KEY = "sonar.aws.sns.region";
    public static final String AWS_SNS_ACCESS_KEY_CONFIG_KEY = "sonar.aws.sns.accessKey";
    public static final String AWS_SNS_SECRET_KEY_CONFIG_KEY = "sonar.aws.sns.secretKey";
    public static final String AWS_SNS_TOPIC_ARN_KEY = "sonar.aws.sns.topicArn";
    public static final String AWS_SNS_ENDPOINT_KEY = "sonar.aws.sns.endpoint";
}
