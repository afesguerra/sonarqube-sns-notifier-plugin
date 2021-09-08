package com.afesguerra.sonarqube.plugin.sns;

import com.afesguerra.sonarqube.plugin.sns.sns.AmazonSNSClientProxy;
import com.amazonaws.services.sns.AmazonSNS;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.ce.posttask.Branch;
import org.sonar.api.ce.posttask.CeTask;
import org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester;
import org.sonar.api.ce.posttask.QualityGate;
import org.sonar.api.config.Configuration;

import java.util.Date;
import java.util.Optional;

import static com.afesguerra.sonarqube.plugin.sns.SNSNotificationPlugin.AWS_SNS_TOPIC_ARN_KEY;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester.newBranchBuilder;
import static org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester.newCeTaskBuilder;
import static org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester.newConditionBuilder;
import static org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester.newProjectBuilder;
import static org.sonar.api.ce.posttask.PostProjectAnalysisTaskTester.newQualityGateBuilder;

@ExtendWith(MockitoExtension.class)
class SNSNotificationTaskTest {

    private static final String TOPIC = "topic";
    private SNSNotificationTask task;

    @Mock
    private Configuration configuration;

    @Mock
    private AmazonSNS sns;

    @Mock
    private AmazonSNSClientProxy snsProxy;

    @BeforeEach
    public void setUp() {
        when(snsProxy.get()).thenReturn(sns);
        when(configuration.get(AWS_SNS_TOPIC_ARN_KEY)).thenReturn(Optional.of(TOPIC));
        task = new SNSNotificationTask(configuration, snsProxy);
    }

    @Test
    void testSNSNotificationTask() {
        PostProjectAnalysisTaskTester.of(task)
                .withCeTask(newCeTaskBuilder()
                        .setId("id")
                        .setStatus(CeTask.Status.SUCCESS)
                        .build())
                .withProject(newProjectBuilder()
                        .setUuid("uuid")
                        .setKey("key")
                        .setName("name")
                        .build())
                .at(new Date())
                .withAnalysisUuid("uuid")
                .withBranch(newBranchBuilder()
                        .setName("master")
                        .setIsMain(true)
                        .setType(Branch.Type.BRANCH)
                        .build())
                .withQualityGate(newQualityGateBuilder()
                        .setId("id")
                        .setName("name")
                        .setStatus(QualityGate.Status.OK)
                        .add(newConditionBuilder()
                                .setMetricKey("coverage")
                                .setOperator(QualityGate.Operator.GREATER_THAN)
                                .setErrorThreshold("12")
                                .build(QualityGate.EvaluationStatus.OK, "35"))
                        .build())
                .execute();

        verify(sns).publish(eq(TOPIC), any(String.class));
    }
}
