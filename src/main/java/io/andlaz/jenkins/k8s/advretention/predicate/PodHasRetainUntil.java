package io.andlaz.jenkins.k8s.advretention.predicate;

import io.andlaz.jenkins.k8s.advretention.WhenLabeledToDoSo;
import io.fabric8.kubernetes.api.model.Pod;

import java.util.function.Predicate;

public class PodHasRetainUntil implements Predicate<Pod> {

    @Override
    public boolean test(Pod pod) {

        return pod.getMetadata() != null &&
                pod.getMetadata().getLabels() != null &&
                pod.getMetadata().getAnnotations().containsKey(WhenLabeledToDoSo.ANNOTATION_RETAIN_UNTIL);
    }
}
