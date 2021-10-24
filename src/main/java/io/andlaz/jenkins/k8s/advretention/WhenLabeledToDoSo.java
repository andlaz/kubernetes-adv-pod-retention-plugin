package io.andlaz.jenkins.k8s.advretention;

import hudson.Extension;
import io.andlaz.jenkins.k8s.advretention.predicate.PodHasRetain;
import io.andlaz.jenkins.k8s.advretention.work.RetainedPodCleanup;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClientException;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud;
import org.csanchez.jenkins.plugins.kubernetes.pod.retention.PodRetention;
import org.csanchez.jenkins.plugins.kubernetes.pod.retention.PodRetentionDescriptor;
import org.jenkinsci.Symbol;
import org.jenkinsci.plugins.kubernetes.auth.KubernetesAuthException;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WhenLabeledToDoSo extends PodRetention implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(WhenLabeledToDoSo.class.getName());

    @Extension
    @Symbol("whenLabeled")
    public static class DescriptorImpl extends PodRetentionDescriptor {

    }

    @DataBoundConstructor
    public WhenLabeledToDoSo() {
    }

    public static final String LABEL_RETAIN = "andlaz.io/retain";
    public static final String ANNOTATION_RETAIN_UNTIL = "andlaz.io/retain-until";

    @Override
    public boolean shouldDeletePod(KubernetesCloud cloud, Pod pod) {
        try {
            LOGGER.log(Level.FINEST, "Looking at {0} for label {1}", new Object[]{pod, WhenLabeledToDoSo.LABEL_RETAIN});
            return new PodHasRetain().test(pod) == false;
        } catch (KubernetesClientException e) {
            LOGGER.log(Level.SEVERE, "Failed to update pod " + pod, e);
        }

        return true;
    }
}
