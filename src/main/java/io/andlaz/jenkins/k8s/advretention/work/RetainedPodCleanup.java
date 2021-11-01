package io.andlaz.jenkins.k8s.advretention.work;

import hudson.Extension;
import hudson.model.AperiodicWork;
import io.andlaz.jenkins.k8s.advretention.WhenLabeledToDoSo;
import io.andlaz.jenkins.k8s.advretention.config.AdvancedPodRetentionPoliciesConfiguration;
import io.andlaz.jenkins.k8s.advretention.predicate.PodNeedsCleanup;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.client.KubernetesClient;
import jenkins.model.Jenkins;
import org.csanchez.jenkins.plugins.kubernetes.KubernetesCloud;
import org.jenkinsci.plugins.kubernetes.auth.KubernetesAuthException;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/*
    Finds pods with retain-until timestamps in the past,
    deletes them
 */
@Extension
public class RetainedPodCleanup extends AperiodicWork {

    private List<KubernetesCloud> clouds = Collections.emptyList();
    private static final Logger LOGGER = Logger.getLogger(RetainedPodCleanup.class.getName());

    @Inject
    private AdvancedPodRetentionPoliciesConfiguration config;

    public RetainedPodCleanup() {
    }

    public RetainedPodCleanup(List<KubernetesCloud> clouds, AdvancedPodRetentionPoliciesConfiguration config) {
        this.clouds = clouds;
        this.config = config;

        LOGGER.log(Level.FINEST, "Configured with clouds " + clouds);
    }

    @Override
    public long getRecurrencePeriod() {
        return config.getCleanupConfiguration().getRecurrenceMinutes() * 60 * 1000;
    }

    @Override
    public AperiodicWork getNewInstance() {

        return new RetainedPodCleanup(Jenkins.get().clouds
                .stream()
                .filter(cloud -> KubernetesCloud.class.isAssignableFrom(cloud.getClass()))
                .map(cloud -> (KubernetesCloud)cloud)
                .collect(Collectors.toList()), config);

    }

    @Override
    protected void doAperiodicRun() {

        LOGGER.log(Level.FINEST, "Starting run");
        for (KubernetesCloud cloud : clouds) {

            try {
                KubernetesClient client = cloud.connect();
                List<Pod> podsToCleanUp = config.getCleanupConfiguration().getNamespaces().stream()
                        .map(namespace -> namespace.getName())
                        .flatMap(namespace -> {
                                    LOGGER.log(Level.FINEST, "Looking for Pods past their retention in namespace " + namespace + ", " + cloud);
                                    return client.
                                            pods().inNamespace(namespace.toString())
                                            .withLabel(WhenLabeledToDoSo.LABEL_RETAIN)
                                            .list().getItems().stream().filter(new PodNeedsCleanup());

                                }
                        ).collect(Collectors.toList());

                LOGGER.log(Level.INFO, "Cleaning up " + podsToCleanUp);

                client.pods().delete(podsToCleanUp);

            } catch (KubernetesAuthException e) {
                LOGGER.log(Level.SEVERE, "Caught exception while trying to clean up", e);
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Caught exception while trying to clean up", e);
            }

        }

    }
}
