package co.mil.imi.models.flowable;

import org.flowable.engine.*;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HydraUpload {

    private static TaskService taskService;
    private static ProcessEngine processEngine;

    public static void go() throws IOException {
        ProcessEngineConfiguration cfg = new StandaloneProcessEngineConfiguration()
                .setJdbcUrl("jdbc:h2:mem:flowable;DB_CLOSE_DELAY=-1")
                .setJdbcUsername("hydra")
                .setJdbcPassword("")
                .setJdbcDriver("org.h2.Driver")
                .setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngine = cfg.buildProcessEngine();

        RepositoryService repositoryService = processEngine.getRepositoryService();
        Deployment deployment = repositoryService.createDeployment()
                .addClasspathResource("bpmn/upload.bpmn20.xml")
                .deploy();

        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
                .deploymentId(deployment.getId())
                .singleResult();
        System.out.println("Found process definition : " + processDefinition.getName());

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Enter file");
        String file = reader.readLine();
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("file", file);

        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessInstance processInstance =
                runtimeService.startProcessInstanceByKey("upload", processVariables);

        Task task = printTasks(reader);
        processVariables = taskService.getVariables(task.getId());

        System.out.println("Hello " + processVariables.get("auditor") + ", file " + processVariables.get("file")
                + " just arrived, do you approve this?");
        boolean approved = reader.readLine().toLowerCase().equals("y");
        processVariables.put("approved", approved);
        taskService.complete(task.getId(), processVariables);

        task = printTasks(reader);
        processVariables = taskService.getVariables(task.getId());
        if (approved) {
            System.out.println("Hello " + processVariables.get("auditor") + ", por favor evalua la fuente de " +
                    "informacion del archivo " + processVariables.get("file"));
            String aux = reader.readLine();
            processVariables.put("source", aux);
            taskService.complete(task.getId(), processVariables);

            task = printTasks(reader);
            processVariables = taskService.getVariables(task.getId());
            System.out.println("Hello " + processVariables.get("auditor") + ", por favor evalua la informacion " +
                    "del archivo " + processVariables.get("file"));
            aux = reader.readLine();
            processVariables.put("validation", aux);
            taskService.complete(task.getId(), processVariables);

            task = printTasks(reader);
            processVariables = taskService.getVariables(task.getId());
            System.out.println("Hello " + processVariables.get("auditor") + ", por favor asigna la etiqueta segun " +
                    "fenomeno social del archivo " + processVariables.get("file"));
            aux = reader.readLine();
            processVariables.put("tag", aux);
            taskService.complete(task.getId(), processVariables);

            task = printTasks(reader);
            processVariables = taskService.getVariables(task.getId());
            System.out.println("Hello " + processVariables.get("auditor") + ", por favor defina quien tiene " +
                    "acceso al archivo " + processVariables.get("file"));
            aux = reader.readLine();
            processVariables.put("access", aux);
        }
        else {
            System.out.println("Hello " + processVariables.get("auditor") + ", por favor emita concepto de mejora " +
                    "del archivo " + processVariables.get("file"));
            String aux = reader.readLine();
            processVariables.put("improvement", aux);
        }
        taskService.complete(task.getId(), processVariables);


        HistoryService historyService = processEngine.getHistoryService();
        List<HistoricActivityInstance> activities =
                historyService.createHistoricActivityInstanceQuery()
                        .processInstanceId(processInstance.getId())
                        .finished()
                        .orderByHistoricActivityInstanceEndTime().asc()
                        .list();

        for (HistoricActivityInstance activity : activities)
            System.out.println(activity.getActivityId() + " took "
                    + activity.getDurationInMillis() + " milliseconds");

        System.out.println("\nVariables recolestadas para el archivo " + processVariables.get("file"));
        System.out.println(processVariables);
    }

    private static Task printTasks(BufferedReader reader) throws IOException {
        taskService = processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup("auditors").list();
        System.out.println("You have " + tasks.size() + " tasks:");
        for (int i = 0; i < tasks.size(); i++)
            System.out.println((i + 1) + ") " + tasks.get(i).getName());

        System.out.println("Which task would you like to complete?");
        int taskIndex = Integer.parseInt(reader.readLine());
        return tasks.get(taskIndex - 1);
    }

    public static void main(String[] args) throws IOException {
        go();
    }
}
