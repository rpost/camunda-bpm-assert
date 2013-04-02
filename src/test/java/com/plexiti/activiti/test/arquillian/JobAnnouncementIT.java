package com.plexiti.activiti.test.arquillian;

import org.camunda.bpm.engine.test.fluent.FluentProcessEngineTestRule;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static com.plexiti.activiti.showcase.jobannouncement.process.ProcessConstants.*;
import static org.camunda.bpm.engine.test.fluent.FluentProcessEngineTests.*;

@RunWith(Arquillian.class)
public class JobAnnouncementIT {

  @Deployment
  public static WebArchive createDeployment() {
     return prepareDeployment("job-announcement-test.war")
          .addAsResource("com/plexiti/activiti/showcase/jobannouncement/process/job-announcement.bpmn")
          .addAsResource("com/plexiti/activiti/showcase/jobannouncement/process/job-announcement-publication.bpmn");
  }

  @Rule
  public FluentProcessEngineTestRule bpmnFluentTestRule = new FluentProcessEngineTestRule(this);

  @Test
  public void testEndToEnd() throws InterruptedException {

    long jobAnnouncementId = 1L;
    newProcessInstance(JOBANNOUNCEMENT_PROCESS)
      .withVariable("jobAnnouncementId", jobAnnouncementId)
      .start();
    System.out.println("Started process instance id " + jobAnnouncementId);

    assertThat(processInstance())
      .isStarted()
      .isWaitingAt(TASK_DESCRIBE_POSITION);
    assertThat(processInstance().task())
      .hasCandidateGroup(ROLE_STAFF)
      .isUnassigned();

  }

}
