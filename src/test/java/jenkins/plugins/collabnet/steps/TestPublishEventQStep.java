package jenkins.plugins.collabnet.steps;

import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import hudson.model.Result;
import jenkins.plugins.collabnet.steps.PublishEventQStep;

public class TestPublishEventQStep {
    
    @Rule
    public JenkinsRule jenkins = new JenkinsRule();

    @Test
    public void buildWithEmptyServerUrlMustFail() throws Exception {
        WorkflowJob p = jenkins.jenkins.createProject(WorkflowJob.class, "p");
        
        p.setDefinition(new CpsFlowDefinition(
                "publishEventQ serverUrl: '', sourceKey: '1234', markUnstable: true"
        ));
        WorkflowRun b1 = p.scheduleBuild2(0).waitForStart();
        jenkins.assertBuildStatus(Result.UNSTABLE, jenkins.waitForCompletion(b1));
        jenkins.assertLogContains(PublishEventQStep.PublishEventQStepExecution.LOG_MESSAGE_INVALID_URL, b1);
    }

    @Test
    public void buildWithEmptySourceKeyMustFail() throws Exception {
        WorkflowJob p = jenkins.jenkins.createProject(WorkflowJob.class, "p");
        
        p.setDefinition(new CpsFlowDefinition(
                "publishEventQ serverUrl: 'amqp://mq.example.com:5672', sourceKey: '', markUnstable: true"
        ));
        WorkflowRun b1 = p.scheduleBuild2(0).waitForStart();
        jenkins.assertBuildStatus(Result.UNSTABLE, jenkins.waitForCompletion(b1));
        jenkins.assertLogContains(PublishEventQStep.PublishEventQStepExecution.LOG_MESSAGE_INVALID_SOURCE_KEY, b1);
    }
}
