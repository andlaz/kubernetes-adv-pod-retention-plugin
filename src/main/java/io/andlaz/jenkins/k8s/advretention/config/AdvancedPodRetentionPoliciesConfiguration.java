package io.andlaz.jenkins.k8s.advretention.config;

import hudson.Extension;
import jenkins.model.GlobalConfiguration;
import net.sf.json.JSONObject;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.DataBoundSetter;
import org.kohsuke.stapler.StaplerRequest;

@Symbol("advPodRetention")
@Extension
public class AdvancedPodRetentionPoliciesConfiguration extends GlobalConfiguration {

    private Boolean manageCleanup = true;
    private CleanupConfiguration cleanupConfiguration = new CleanupConfiguration();

    @Override
    public boolean configure(StaplerRequest req, JSONObject json) throws FormException {
        req.bindJSON(this, json);
        return super.configure(req, json);
    }

    public Boolean getManageCleanup() {
        return manageCleanup;
    }

    @DataBoundSetter
    public void setManageCleanup(Boolean manageCleanup) {
        this.manageCleanup = manageCleanup;
    }

    public CleanupConfiguration getCleanupConfiguration() {
        return cleanupConfiguration;
    }

    @DataBoundSetter
    public void setCleanupConfiguration(CleanupConfiguration cleanupConfiguration) {
        this.cleanupConfiguration = cleanupConfiguration;
    }
}
