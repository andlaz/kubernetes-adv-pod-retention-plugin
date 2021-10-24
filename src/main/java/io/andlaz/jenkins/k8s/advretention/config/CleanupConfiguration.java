package io.andlaz.jenkins.k8s.advretention.config;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.DataBoundSetter;

import java.util.Collections;
import java.util.List;

public class CleanupConfiguration extends AbstractDescribableImpl<CleanupConfiguration> {

    private Integer recurrenceMinutes = 5;
    private List<CleanupTargetNamespace> namespaces = Collections.emptyList();

    @Extension
    @Symbol("cleanup")
    public static class DescriptorImpl extends Descriptor<CleanupConfiguration> {

        @Override
        public String getDisplayName() {
            return "Cleanup configuration";
        }

    }

    @DataBoundConstructor
    public CleanupConfiguration() {
    }

    public Integer getRecurrenceMinutes() {
        return recurrenceMinutes;
    }

    @DataBoundSetter
    public void setRecurrenceMinutes(Integer recurrenceMinutes) {
        this.recurrenceMinutes = recurrenceMinutes;
    }

    public List<CleanupTargetNamespace> getNamespaces() {
        return namespaces;
    }

    @DataBoundSetter
    public void setNamespaces(List<CleanupTargetNamespace> namespaces) {
        this.namespaces = namespaces;
    }
}
