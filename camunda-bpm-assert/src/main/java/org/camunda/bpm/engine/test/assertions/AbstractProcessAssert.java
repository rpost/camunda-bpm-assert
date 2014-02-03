package org.camunda.bpm.engine.test.assertions;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.*;
import org.assertj.core.api.AbstractAssert;
import org.camunda.bpm.engine.history.*;
import org.camunda.bpm.engine.repository.ProcessDefinitionQuery;
import org.camunda.bpm.engine.runtime.ExecutionQuery;
import org.camunda.bpm.engine.runtime.JobQuery;
import org.camunda.bpm.engine.runtime.ProcessInstanceQuery;
import org.camunda.bpm.engine.runtime.VariableInstanceQuery;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.task.TaskQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin Schimak <martin.schimak@plexiti.com>
 */
public abstract class AbstractProcessAssert<S extends AbstractProcessAssert<S, A>, A> extends AbstractAssert<S, A> {

  protected ProcessEngine engine;

  private static ThreadLocal<Map<Class<?>, AbstractProcessAssert>>
    lastAsserts = new ThreadLocal<Map<Class<?>, AbstractProcessAssert>>();

  protected AbstractProcessAssert(ProcessEngine engine, A actual, Class<?> selfType) {
    super(actual, selfType);
    this.engine = engine;
    setLastAssert(selfType, this);
  }

  /*
   * Delivers the the actual object under test.
   */
  public A getActual() {
    return actual;
  }

  /*
   * Method definition meant to deliver the current/refreshed persistent state of 
   * the actual object under test and expecting that such a current state actually exists.
   */
  protected A getExistingCurrent() {
    Assertions.assertThat(actual)
      .overridingErrorMessage("Expecting assertion to be called on non-null actual, but found it to be null!")
      .isNotNull();
    A current =getCurrent();
    Assertions.assertThat(current)
      .overridingErrorMessage("Expecting assertion to be called on non-null current state of actual %s, but found it to be null!",
        actual)
      .isNotNull();
    return current;
  }
  
  /*
   * Abstract method definition meant to deliver the current/refreshed persistent state of 
   * the actual object under test. Needs to be correctly implemented by implementations of this.
   */
  protected abstract A getCurrent();
  
  public static void resetLastAsserts() {
    getLastAsserts().clear();
  }

  @SuppressWarnings("unchecked")
  public static <S extends AbstractProcessAssert> S getLastAssert(Class<S> assertClass) {
    return (S) getLastAsserts().get(assertClass);
  }

  private static void setLastAssert(Class<?> assertClass, AbstractProcessAssert assertInstance) {
    getLastAsserts().put(assertClass, assertInstance);
  }

  private static Map<Class<?>, AbstractProcessAssert> getLastAsserts() {
    Map<Class<?>, AbstractProcessAssert> asserts = lastAsserts.get();
    if (asserts == null)
      lastAsserts.set(asserts = new HashMap<Class<?>, AbstractProcessAssert>());
    return asserts;
  }

  protected RepositoryService repositoryService() {
    return engine.getRepositoryService();
  }

  protected RuntimeService runtimeService() {
    return engine.getRuntimeService();
  }

  protected FormService formService() {
    return engine.getFormService();
  }

  protected TaskService taskService() {
    return engine.getTaskService();
  }

  protected HistoryService historyService() {
    return engine.getHistoryService();
  }

  protected IdentityService identityService() {
    return engine.getIdentityService();
  }

  protected ManagementService managementService() {
    return engine.getManagementService();
  }

  protected AuthorizationService authorizationService() {
    return engine.getAuthorizationService();
  }

  /* 
   * TaskQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or {@link ProcessDefinition}) 
   * by overriding this method in sub classes specialised to verify a specific 
   * process engine domain class. 
   */
  protected TaskQuery taskQuery() {
    return taskService().createTaskQuery();
  }

  /* 
   * JobQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or {@link ProcessDefinition}) 
   * by overriding this method in sub classes specialised to verify a specific 
   * process engine domain class. 
   */
  protected JobQuery jobQuery() {
    return managementService().createJobQuery();
  }

  /* 
   * ProcessInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected ProcessInstanceQuery processInstanceQuery() {
    return runtimeService().createProcessInstanceQuery();
  }

  /* 
   * ExecutionQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or {@link ProcessDefinition}) 
   * by overriding this method in sub classes specialised to verify a specific 
   * process engine domain class. 
   */
  protected ExecutionQuery executionQuery() {
    return runtimeService().createExecutionQuery();
  }

  /* 
   * VariableInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected VariableInstanceQuery variableInstanceQuery() {
    return runtimeService().createVariableInstanceQuery();
  }

  /* 
   * HistoricActivityInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected HistoricActivityInstanceQuery historicActivityInstanceQuery() {
    return historyService().createHistoricActivityInstanceQuery();
  }

  /* 
   * HistoricDetailQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected HistoricDetailQuery historicDetailQuery() {
    return historyService().createHistoricDetailQuery();
  }

  /* 
   * HistoricProcessInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected HistoricProcessInstanceQuery historicProcessInstanceQuery() {
    return historyService().createHistoricProcessInstanceQuery();
  }

  /* 
   * HistoricTaskInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected HistoricTaskInstanceQuery historicTaskInstanceQuery() {
    return historyService().createHistoricTaskInstanceQuery();
  }

  /* 
   * HistoricVariableInstanceQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected HistoricVariableInstanceQuery historicVariableInstanceQuery() {
    return historyService().createHistoricVariableInstanceQuery();
  }

  /* 
   * ProcessDefinitionQuery, unnarrowed. Narrow this to {@link ProcessInstance} (or 
   * {@link ProcessDefinition}) by overriding this method in sub classes specialised to 
   * verify a specific process engine domain class. 
   */
  protected ProcessDefinitionQuery processDefinitionQuery() {
    return repositoryService().createProcessDefinitionQuery();
  }

}
