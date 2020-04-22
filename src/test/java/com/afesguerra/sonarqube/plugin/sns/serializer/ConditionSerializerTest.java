package com.afesguerra.sonarqube.plugin.sns.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sonar.api.ce.posttask.QualityGate;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConditionSerializerTest {

    private static final String METRIC_KEY = "LineCoverage";
    private static final String WARNING_THRESHOLD = "75";
    private static final String ERROR_THRESHOLD = "70";
    private static final boolean ON_LEAK_PERIOD = true;
    private static final String VALUE = "80";

    @Mock
    private QualityGate.Condition condition;
    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider serializerProvider;

    private StdSerializer<QualityGate.Condition> serializer;

    @BeforeEach
    public void setUp() {
        serializer = new ConditionSerializer();

        when(condition.getStatus()).thenReturn(QualityGate.EvaluationStatus.OK);
        when(condition.getMetricKey()).thenReturn(METRIC_KEY);
        when(condition.getOperator()).thenReturn(QualityGate.Operator.LESS_THAN);
        when(condition.getWarningThreshold()).thenReturn(WARNING_THRESHOLD);
        when(condition.getErrorThreshold()).thenReturn(ERROR_THRESHOLD);
        when(condition.isOnLeakPeriod()).thenReturn(ON_LEAK_PERIOD);
    }

    @Test
    public void testSerializeNoValue() throws Exception {
        when(condition.getStatus()).thenReturn(QualityGate.EvaluationStatus.NO_VALUE);
        serializer.serialize(condition, gen, serializerProvider);

        verify(condition, never()).getValue();
    }

    @Test
    public void testWithValue() throws Exception {
        when(condition.getValue()).thenReturn(VALUE);
        serializer.serialize(condition, gen, serializerProvider);
        verify(condition).getValue();
    }
}
