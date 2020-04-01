package co.mil.imi.models.resources;

import co.mil.imi.models.flowable.HydraUpload;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.flowable.engine.TaskService;
import org.flowable.task.api.Task;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Path("/upload")
@Produces(MediaType.TEXT_PLAIN)
public class UploadResource {

    @POST
    @Path("/uploadFile")
    public String uploadFile(@FormParam("file") String file) {
        Map<String, Object> processVariables = new HashMap<>();
        processVariables.put("file", file);
        HydraUpload.processEngine.getRuntimeService().startProcessInstanceByKey("upload", processVariables);
        return "All right";
    }

    @GET
    @Path("/getTasks")
    public String getTasks(@QueryParam("candidate_group") String candidateGroup) {
        System.out.println(candidateGroup);
        TaskService taskService = HydraUpload.processEngine.getTaskService();
        List<Task> tasks = taskService.createTaskQuery().taskCandidateGroup(candidateGroup).list();
        System.out.println("You have " + tasks.size() + " tasks:");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            builder.append(i + 1).append(") ").append(task.getName()).append(" with id: ").append(task.getId())
                    .append(" and variables: ").append(taskService.getVariables(task.getId())).append("\n");
        }
        return builder.toString();
    }

    static class Entity {
        @JsonProperty
        String name;
        @JsonProperty
        String value;
    }

    @POST
    @Path("/completeTask")
    @Consumes(MediaType.APPLICATION_JSON)
    public String completeTask(List<Entity> entities) {
        TaskService taskService = HydraUpload.processEngine.getTaskService();
        String id = null;
        HashMap<String, Object> variables = new HashMap<>();
        for (Entity entity: entities) {
            if (entity.name.equals("id"))
                id = entity.value;
            else if (entity.name.equals("approved"))
                variables.put(entity.name, entity.value.equals("true"));
            else
                variables.put(entity.name, entity.value);
        }
        System.out.println(id);
        System.out.println(variables);
        taskService.complete(id, variables);
        return "Task completed";
    }
}
