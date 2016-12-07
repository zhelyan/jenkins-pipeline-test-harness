package mypackage;

import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;

public class MyStepTest extends UserFlowTest {

  @Override
  public String defineFlow() {
    return "node {\n" +
      "def txt = 'hey'\n" +
      "myStep(txt)" +
      "}";
  }

  public void perform() throws Exception {
    WorkflowJob job = createWorkflowProject(false);
    WorkflowRun b = story.j.assertBuildStatusSuccess(job.scheduleBuild2(0));

    story.j.assertLogContains("hey", b);
  }

}
