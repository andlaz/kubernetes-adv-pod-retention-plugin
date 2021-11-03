package io.andlaz.jenkins.k8s.advretention.predicate;

import io.andlaz.jenkins.k8s.advretention.WhenLabeledToDoSo;
import io.fabric8.kubernetes.api.model.Pod;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.function.Predicate;

public class PodNeedsCleanup implements Predicate<Pod> {

    @Override
    public boolean test(Pod pod) {

        return OffsetDateTime.parse(pod.getMetadata().getAnnotations().get(WhenLabeledToDoSo.ANNOTATION_RETAIN_UNTIL), DateTimeFormatter.ISO_OFFSET_DATE_TIME).isBefore(OffsetDateTime.now());

    }
}
