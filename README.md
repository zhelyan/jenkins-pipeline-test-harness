# Jenkins pipeline test harness

How to test pipeline vars and flows POC.


## Adding tests

- subclass from [UserFlowTest](test/groovy/mypackage/UserFlowTest.java)
    - implement `String defineFlow()` - put your `Jenkinsfile` content
      here. This should be limited to the scope of the test of course
      e.g.

```java

  public String defineFlow() {
    return "node { \n" +
      "def text = 'Hello ${name}" +
      "def binding = [name: 'kitty']\n" +
      "echo interpolate(text, binding)\n"+
      "}"
  }

```

- `public void perform()` - this is where you test the result of running
  the flow. See
  [test/groovy/mypackage/MyStepTest.java](test/groovy/mypackage/MyStepTest.java)

Tests for core classes and everything which cannot / should not be
tested in a flow can go to `test/groovy/` or `test/java/`

## Test

Run: `mvn clean test`

It would've helped if
[workflow-cps-global-lib-plugin](https://github.com/jenkinsci/workflow-cps-global-lib-plugin)
expected a standard Maven project structure

