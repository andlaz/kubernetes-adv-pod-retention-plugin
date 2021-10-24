package io.andlaz.jenkins.k8s.advretention.step;

import hudson.Extension;
import hudson.model.Computer;
import org.jenkinsci.plugins.workflow.steps.Step;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.jenkinsci.plugins.workflow.steps.StepDescriptor;
import org.jenkinsci.plugins.workflow.steps.StepExecution;
import org.kohsuke.stapler.DataBoundConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.Set;

public class LabelToRetainUntil extends Step implements Serializable {

    private static final long serialVersionUID = 1L;

    @Extension
    public static class DescriptorImpl extends StepDescriptor {

        @Override
        public Set<? extends Class<?>> getRequiredContext() {
            return Collections.singleton(Computer.class);
        }

        @Override
        public String getFunctionName() {
            return "labelToRetainUntil";
        }
    }

    private final Integer minutes;

    @DataBoundConstructor
    public LabelToRetainUntil(Integer minutes) {
        this.minutes = minutes;
    }

    public Integer getMinutes() {
        return minutes;
    }

    @Override
    public StepExecution start(StepContext context) throws Exception {
        return new LabelToRetainUntilExecution(context, this);
    }
}
