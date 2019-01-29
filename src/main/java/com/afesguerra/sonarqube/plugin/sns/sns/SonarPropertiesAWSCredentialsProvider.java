package com.afesguerra.sonarqube.plugin.sns.sns;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.sonar.api.ExtensionPoint;
import org.sonar.api.ce.ComputeEngineSide;
import org.sonar.api.config.Configuration;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_ACCESS_KEY_CONFIG_KEY;
import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_SECRET_KEY_CONFIG_KEY;

@ComputeEngineSide
@ExtensionPoint
public class SonarPropertiesAWSCredentialsProvider implements AWSCredentialsProvider {
    private final Configuration configuration;
    private AWSCredentials credentials;

    public SonarPropertiesAWSCredentialsProvider(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public AWSCredentials getCredentials() {
        if (credentials == null) {
            refresh();
        }
        return credentials;
    }

    @Override
    public void refresh() {
        final String accessKey = getSonarProperty(AWS_SNS_ACCESS_KEY_CONFIG_KEY);
        final String secretKey = getSonarProperty(AWS_SNS_SECRET_KEY_CONFIG_KEY);

        credentials = new BasicAWSCredentials(accessKey, secretKey);
    }

    private String getSonarProperty(final String propertyName) {
        return configuration.get(propertyName)
                .orElseThrow(() -> {
                    final String exceptionMessage = String.format("Must define property %s", propertyName);
                    return new RuntimeException(exceptionMessage);
                });
    }
}
