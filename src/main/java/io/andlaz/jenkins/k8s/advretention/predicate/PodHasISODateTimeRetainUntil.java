package io.andlaz.jenkins.k8s.advretention.predicate;

import io.andlaz.jenkins.k8s.advretention.WhenLabeledToDoSo;
import io.fabric8.kubernetes.api.model.Pod;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.function.Predicate;

public class PodHasISODateTimeRetainUntil implements Predicate<Pod> {

    @Override
    public boolean test(Pod pod) {

        try {
            OffsetDateTime.parse(pod.getMetadata().getAnnotations().get(WhenLabeledToDoSo.ANNOTATION_RETAIN_UNTIL));
        } catch (DateTimeParseException e) {
            return false;
        }

        return true;

    }


}
