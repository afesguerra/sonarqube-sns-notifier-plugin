package com.afesguerra.sonarqube.plugin.sns.sns;

import com.amazonaws.auth.AWSCredentials;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.config.Configuration;

import java.util.Optional;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_ACCESS_KEY_CONFIG_KEY;
import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_SECRET_KEY_CONFIG_KEY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SonarPropertiesAWSCredentialsProviderTest {

    @Mock
    private Configuration configuration;

    private SonarPropertiesAWSCredentialsProvider sonarPropertiesAWSCredentialsProvider;

    @BeforeEach
    void setUp() {
        sonarPropertiesAWSCredentialsProvider = new SonarPropertiesAWSCredentialsProvider(configuration);
    }

    @Test
    void testCredentialsCache() {
        final String accessKey = "accessKey";
        final String secretKey = "secretKey";
        when(configuration.get(AWS_SNS_ACCESS_KEY_CONFIG_KEY)).thenReturn(Optional.of(accessKey));
        when(configuration.get(AWS_SNS_SECRET_KEY_CONFIG_KEY)).thenReturn(Optional.of(secretKey));

        AWSCredentials credentials = sonarPropertiesAWSCredentialsProvider.getCredentials();
        assertEquals(accessKey, credentials.getAWSAccessKeyId());
        assertEquals(secretKey, credentials.getAWSSecretKey());

        final String newAccessKeyValue = "Other";
        final String newSecretKeyValue = "String";

        when(configuration.get(AWS_SNS_ACCESS_KEY_CONFIG_KEY)).thenReturn(Optional.of(newAccessKeyValue));
        when(configuration.get(AWS_SNS_SECRET_KEY_CONFIG_KEY)).thenReturn(Optional.of(newSecretKeyValue));

        credentials = sonarPropertiesAWSCredentialsProvider.getCredentials();
        assertEquals(accessKey, credentials.getAWSAccessKeyId());
        assertEquals(secretKey, credentials.getAWSSecretKey());

        sonarPropertiesAWSCredentialsProvider.refresh();
        credentials = sonarPropertiesAWSCredentialsProvider.getCredentials();
        assertEquals(newAccessKeyValue, credentials.getAWSAccessKeyId());
        assertEquals(newSecretKeyValue, credentials.getAWSSecretKey());
    }
}