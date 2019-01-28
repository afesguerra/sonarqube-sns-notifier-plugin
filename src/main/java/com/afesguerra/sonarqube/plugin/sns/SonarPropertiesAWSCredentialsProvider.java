package com.afesguerra.sonarqube.plugin.sns;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.config.Settings;

import java.util.Objects;

@ComputeEngineSide
@ExtensionPoint
public class SonarPropertiesAWSCredentialsProvider implements AWSCredentialsProvider {
    private final Settings settings;

    public SonarPropertiesAWSCredentialsProvider(Settings settings) {
        this.settings = settings;
    }

    @Override
    public AWSCredentials getCredentials() {
        final String accessKey = settings.getString(SNSNotificationPluginConstants.AWS_SNS_ACCESS_KEY_CONFIG_KEY);
        final String secretKey = settings.getString(SNSNotificationPluginConstants.AWS_SNS_SECRET_KEY_CONFIG_KEY);

        Objects.requireNonNull(accessKey);
        Objects.requireNonNull(secretKey);

        return new BasicAWSCredentials(accessKey, secretKey);
    }

    @Override
    public void refresh() {
        // No op
    }
}
