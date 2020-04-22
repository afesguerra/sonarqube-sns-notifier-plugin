package com.afesguerra.sonarqube.plugin.sns;

import org.junit.jupiter.api.Test;
import org.sonar.api.Plugin;
import org.sonar.api.SonarEdition;
import org.sonar.api.SonarQubeSide;
import org.sonar.api.SonarRuntime;
import org.sonar.api.internal.SonarRuntimeImpl;
import org.sonar.api.utils.Version;

import static org.assertj.core.api.Assertions.assertThat;


public class SNSNotificationPluginTest {

    private Plugin plugin = new SNSNotificationPlugin();

    @Test
    public void testDefine() {
        Version version = Version.create(7, 9);
        SonarRuntime runtime = SonarRuntimeImpl.forSonarQube(version, SonarQubeSide.COMPUTE_ENGINE, SonarEdition.COMMUNITY);
        Plugin.Context context = new Plugin.Context(runtime);
        plugin.define(context);
        assertThat(context.getExtensions()).hasSize(8);
    }
}
