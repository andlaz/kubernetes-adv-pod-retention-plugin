package io.andlaz.jenkins.k8s.advretention.config;

import hudson.Extension;
import hudson.model.AbstractDescribableImpl;
import hudson.model.Descriptor;
import org.kohsuke.stapler.DataBoundConstructor;

public class CleanupTargetNamespace extends AbstractDescribableImpl<CleanupTargetNamespace> {

    private final String name;

    @Extension
    public static class DescriptorImpl extends Descriptor<CleanupTargetNamespace> {

        @Override
        public String getDisplayName() {
            return "Namespace";
        }

    }

    @DataBoundConstructor
    public CleanupTargetNamespace(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
