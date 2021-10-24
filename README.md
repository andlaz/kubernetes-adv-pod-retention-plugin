note: while my understanding of the extension points and core API involved
settles, this is **a proof of concept** and it will receive force pushed amended commits

This plugin provides:

- a [PodRetentionPolicy]() implementation, [WhenLabeledToDoSo](src/main/java/io/andlaz/jenkins/k8s/advretention/WhenLabeledToDoSo.java), which 
  retains a `Pod` when it has a `andlaz.io/jenkins/k8s/advretention/retain-until` label.
- a [Step]() implementation, [LabelToRetainUntil](src/main/java/io/andlaz/jenkins/k8s/advretention/step/LabelToRetainUntil.java) ( dsl : `labelToRetainUntil` ) that adds a label to the `Pod`
  the agent is running in
- a configurable [AperiodicWork]() implementation, [RetainedPodCleanup](), to delete pods
  that have the label `andlaz.io/retain-until` with an `ISO_DATE_TIME` format
  value that's in the past

## Setup

### Jcasc configuration snippets

If you are OK with the Jenkins coordinator performing cleanup of your retained pods,
make sure to list the namespaces you will be needing cleanup to run on

```yaml
unclassified:
  advPodRetention:
    manageCleanup: true
    cleanupConfiguration:
      recurrenceMinutes: 5 # nb: waits for previous cleanup
      namespaces:
        - name: "jenkins"
```

if you'd rather do the cleanups externally ( say, a `CronJob` starting a scripted kubectl call )

```yaml
unclassified:
  advPodRetention:
    manageCleanup: false
```

## Usage
### Label pod in declarative pipeline

The typical use-case is to mark the pod for retention, as needed
```groovy
agent {
  kubernetes {
    // ..
    podRetention whenLabeled()
  }
}

// ..

  script {

    if ( somethingBadNeedHuman ) {

      // let's keep this pod on ice for 2 hours
      // ( PRs for 15 minutes )
      // while our engineer investigates
      labelToRetainUntil minutes: (pr?15:120)
      
      // TODO send email alert to list

    }
  }

```