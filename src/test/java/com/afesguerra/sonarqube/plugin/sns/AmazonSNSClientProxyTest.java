package com.afesguerra.sonarqube.plugin.sns;

import com.amazonaws.services.sns.AmazonSNS;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.sonar.api.config.Settings;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPluginConstants.AWS_SNS_ENDPOINT_KEY;
import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPluginConstants.AWS_SNS_REGION_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AmazonSNSClientProxyTest {

    private static final String AWS_REGION = "eu-west-1";

    @Mock
    private Settings settings;

    @Mock
    private SonarPropertiesAWSCredentialsProvider credentialsProvider;

    private AmazonSNSClientProxy proxy;

    @Before
    public void setUp() throws Exception {
        proxy = new AmazonSNSClientProxy(settings, credentialsProvider);
    }

    @Test
    public void isSingleton() throws Exception {
        when(settings.getString(AWS_SNS_REGION_KEY)).thenReturn(AWS_REGION);
        when(settings.getString(AWS_SNS_ENDPOINT_KEY)).thenReturn(null);

        final AmazonSNS sns1 = proxy.get();
        final AmazonSNS sns2 = proxy.get();

        assertThat(sns1 == sns2).isTrue();
    }

}
