package io.andlaz.jenkins.k8s.advretention;

import io.fabric8.kubernetes.api.model.Pod;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WhenLabeledToDoSoTest extends AdvancedRetentionPoliciesTestSuite {

    @Test
    public void test_shouldDeletePod_returns_true_when_pod_not_labeled() {

        WhenLabeledToDoSo retention = new WhenLabeledToDoSo();
        assertTrue(retention.shouldDeletePod(mock(KubernetesCloud.class), stubPodWithLabels(Collections.emptyMap())));

    }

    @Test
    public void test_shouldDeletePod_returns_false_when_pod_labeled() {

        WhenLabeledToDoSo retention = new WhenLabeledToDoSo();
        assertFalse(retention.shouldDeletePod(mock(KubernetesCloud.class), stubPodWithLabels(new HashMap<String, String>() {{
            put(WhenLabeledToDoSo.LABEL_RETAIN, "value-irrelevant");
        }})));

    }

}