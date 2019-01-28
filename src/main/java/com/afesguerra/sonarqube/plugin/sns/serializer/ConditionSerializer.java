package com.afesguerra.sonarqube.plugin.sns.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.sonar.api.ce.posttask.QualityGate;

import java.io.IOException;

public class ConditionSerializer extends StdSerializer<QualityGate.Condition> {
    private static final String STATUS_KEY = "status";
    private static final String METRIC_KEY_KEY = "metricKey";
    private static final String OPERATOR_KEY = "operator";
    private static final String ERROR_THRESHOLD_KEY = "errorThreshold";
    private static final String WARNING_THRESHOLD_KEY = "warningThreshold";
    private static final String LEAK_PERIOD_KEY = "leakPeriod";
    private static final String VALUE_KEY = "value";

    public ConditionSerializer() {
        this(null);
    }

    private ConditionSerializer(Class<QualityGate.Condition> t) {
        super(t);
    }

    @Override
    public void serialize(QualityGate.Condition condition, JsonGenerator gen, SerializerProvider provider) throws IOException {
        final QualityGate.EvaluationStatus status = condition.getStatus();
        final String metricKey = condition.getMetricKey();
        final QualityGate.Operator operator = condition.getOperator();
        final String errorThreshold = condition.getErrorThreshold();
        final String warningThreshold = condition.getWarningThreshold();
        final boolean onLeakPeriod = condition.isOnLeakPeriod();

        gen.writeStartObject();
        gen.writeObjectField(STATUS_KEY, status);
        gen.writeObjectField(METRIC_KEY_KEY, metricKey);
        gen.writeObjectField(OPERATOR_KEY, operator);
        gen.writeObjectField(ERROR_THRESHOLD_KEY, errorThreshold);
        gen.writeObjectField(WARNING_THRESHOLD_KEY, warningThreshold);
        gen.writeBooleanField(LEAK_PERIOD_KEY, onLeakPeriod);

        if (!QualityGate.EvaluationStatus.NO_VALUE.equals(status)) {
            gen.writeObjectField(VALUE_KEY, condition.getValue());
        } else {
            gen.writeNullField(VALUE_KEY);
        }

        gen.writeEndObject();
    }
}
