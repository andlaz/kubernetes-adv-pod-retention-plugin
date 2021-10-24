package io.andlaz.jenkins.k8s.advretention;

import io.fabric8.kubernetes.api.model.ObjectMeta;
import io.fabric8.kubernetes.api.model.Pod;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public abstract class AdvancedRetentionPoliciesTestSuite {

    protected Map<String, String> retentionLabel(ZonedDateTime retainUntil) {
        return new HashMap<String, String>() {{
            put(WhenLabeledToDoSo.LABEL_RETAIN,retainUntil.format(DateTimeFormatter.ISO_DATE_TIME));
        }};
    }

    protected Pod stubPodWithLabels(Map<String, String> labels) {

        ObjectMeta meta = mock(ObjectMeta.class);
        Pod pod = mock(Pod.class);
        when(pod.getMetadata()).thenReturn(meta);
        when(meta.getLabels()).thenReturn(labels);

        return pod;

    }

    protected void stubRetainedPod(ZonedDateTime retainUntil) {

        stubPodWithLabels(retentionLabel(retainUntil));

    }

}
