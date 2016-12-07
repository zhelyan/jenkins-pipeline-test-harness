package mypackage;

import jenkins.model.Jenkins;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomUtils;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.cps.global.UserDefinedGlobalVariableList;
import org.jenkinsci.plugins.workflow.cps.global.WorkflowLibRepository;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.model.Statement;
import org.jvnet.hudson.test.RestartableJenkinsRule;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;


public abstract class UserFlowTest extends Assert {

  @Rule
  public RestartableJenkinsRule story = new RestartableJenkinsRule();

  @Inject
  Jenkins jenkins;

  @Inject
  WorkflowLibRepository repo;

  @Inject
  UserDefinedGlobalVariableList uvl;


  /**
   * User can define global variables.
   */
  @Test
  public void test() {
    story.addStep(new Statement() {
                    @Override
                    public void evaluate() throws Throwable {
                      copySources();
                      // simulate the effect of push
                      uvl.rebuild();
                      System.out.println("* Testing " + flowName());
                      //run implementation
                      perform();
                    }
                  }

    );
  }

  protected WorkflowJob createWorkflowProject(boolean inSandbox) throws IOException {
    WorkflowJob p = jenkins.createProject(WorkflowJob.class, flowName() + "-" + RandomUtils.nextInt(0,9^3));
    p.setDefinition(new CpsFlowDefinition(defineFlow(), inSandbox));
    return p;
  }

  /*
  Subclasses must define the flow (content in the Jenkinsfile) which will be executed
   */
  public abstract String defineFlow();

  /*
  Subclasses must implement the test
   */
  public abstract void perform() throws Exception;


  /*
  Defaults to implementor's name with the suffix Test removed
  E.g MyVarTest would yield MyVar
   */
  protected String flowName() {
    String className = this.getClass().getSimpleName().replaceAll("Test$", "");
    return className.substring(0, 1).toLowerCase() + className.substring(1);
  }

  protected void copySources() {
    try {
      FileUtils.copyDirectory(new File("vars"), new File(repo.workspace, "vars"));
      FileUtils.copyDirectory(new File("src"), new File(repo.workspace, "src"));
    } catch (IOException e) {
      e.printStackTrace();
      System.exit(1);
    }
  }


}
