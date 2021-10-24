package io.andlaz.jenkins.k8s.advretention.step;

import hudson.model.Computer;
import hudson.model.Node;
import io.andlaz.jenkins.k8s.advretention.WhenLabeledToDoSo;
import io.fabric8.kubernetes.api.model.PodBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesComputer;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesSlave;
import org.jenkinsci.plugins.workflow.steps.GeneralNonBlockingStepExecution;
import org.jenkinsci.plugins.workflow.steps.StepContext;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class LabelToRetainUntilExecution extends GeneralNonBlockingStepExecution {

    private final LabelToRetainUntil step;

    public LabelToRetainUntilExecution(StepContext context, LabelToRetainUntil step) {
        super(context);
        this.step = step;
    }

    @Override
    public boolean start() throws Exception {

        run(() -> {
            Node node = getContext().get(Node.class);

            if (KubernetesSlave.class.isAssignableFrom(node.getClass())) {
                KubernetesSlave kubernetesNode = (KubernetesSlave) node;

                KubernetesClient client = kubernetesNode.getKubernetesCloud().connect();
                client.pods().inNamespace(kubernetesNode.getNamespace()).withName(kubernetesNode.getPodName()).edit(pod ->
                        new PodBuilder(pod).editOrNewMetadata()
                                .addToLabels(WhenLabeledToDoSo.LABEL_RETAIN, "")
                                .addToAnnotations(WhenLabeledToDoSo.ANNOTATION_RETAIN_UNTIL,
                                        ZonedDateTime.now().plusMinutes(step.getMinutes()).format(DateTimeFormatter.ISO_DATE_TIME)).endMetadata().build()
                );

                getContext().onSuccess(null);

            }

            getContext().onFailure(new IllegalStateException("No pod detected"));

        });

        return false;

    }
}
