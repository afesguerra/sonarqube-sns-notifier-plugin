package com.afesguerra.sonarqube.plugin.sns;

import org.junit.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;


public class SNSNotificationPluginTest {

    private Plugin plugin = new SNSNotificationPlugin();

    @Test
    public void testDefine() {
        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(Version.create(6, 7), SonarQubeSide.COMPUTE_ENGINE);
        Plugin.Context context = new Plugin.Context(runtime);
        plugin.define(context);
        assertThat(context.getExtensions()).hasSize(8);
    }
}
