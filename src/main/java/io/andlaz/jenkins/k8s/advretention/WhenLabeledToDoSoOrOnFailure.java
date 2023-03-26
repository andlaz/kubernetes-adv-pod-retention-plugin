package io.andlaz.jenkins.k8s.advretention;

import hudson.Extension;
import io.fabric8.kubernetes.api.model.Pod;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud;
import org.csanchez.jenkins.plugins.kubernetes.pod.retention.OnFailure;
import org.csanchez.jenkins.plugins.kubernetes.pod.retention.PodRetention;
import org.csanchez.jenkins.plugins.kubernetes.pod.retention.PodRetentionDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.util.function.Supplier;

public class WhenLabeledToDoSoOrOnFailure extends PodRetention implements Serializable {

    private static final long serialVersionUID = 1L;
    private final PodRetention whenLabeledToDoSo = new WhenLabeledToDoSo();
    private final PodRetention onFailure = new OnFailure();

    @DataBoundConstructor
    public WhenLabeledToDoSoOrOnFailure() {
    }

    @Override
    public boolean shouldDeletePod(KubernetesCloud cloud, Supplier<Pod> pod) {
        return whenLabeledToDoSo.shouldDeletePod(cloud, pod) || onFailure.shouldDeletePod(cloud, pod);
    }

    @Extension
    @Symbol("whenLabeledOrOnFailure")
    public static class DescriptorImpl extends PodRetentionDescriptor {

    }
}
