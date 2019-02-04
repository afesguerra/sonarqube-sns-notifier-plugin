package com.afesguerra.sonarqube.plugin.sns.sns;

import com.amazonaws.services.sns.AmazonSNS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.config.Configuration;

import java.util.Optional;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_ENDPOINT_KEY;
import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_REGION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmazonSNSClientProxyTest {

    private static final String AWS_REGION = "eu-west-1";

    @Mock
    private Configuration configuration;

    @Mock
    private SonarPropertiesAWSCredentialsProvider credentialsProvider;

    private AmazonSNSClientProxy proxy;

    @Before
    public void setUp() {
        proxy = new AmazonSNSClientProxy(configuration, credentialsProvider);
    }

    @Test
    public void isSingleton() {
        when(configuration.get(AWS_SNS_REGION_KEY)).thenReturn(Optional.of(AWS_REGION));
        when(configuration.get(AWS_SNS_ENDPOINT_KEY)).thenReturn(Optional.empty());

        final AmazonSNS sns1 = proxy.get();
        final AmazonSNS sns2 = proxy.get();

        assertThat(sns1 == sns2).isTrue();
    }

}
