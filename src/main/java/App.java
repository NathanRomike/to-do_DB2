import java.lang.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    staticFileLocation("/public");
    String layout = "templates/layout.vtl";

// READ ALL TASKS LIST

    get("/tasks", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Task> tasks = Task.all();
      model.put("tasks", tasks);
      model.put("template", "templates/index.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// CREATE TASK TO TASKS LIST

    post("/tasks/create", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String description = request.queryParams("description");
      Task newTask = new Task(description);
      newTask.save();
      response.redirect("/tasks");
      return null;
    });

// // DELETE TASK TO TASKS PAGE
//
//     post("/tasks/delete", (request, response) -> {
//       HashMap<String, Object> model = new HashMap<String, Object>();
//       Task task = Task.find(Integer.parseInt(request.params("id")));
//       task.delete();
//       response.redirect("/tasks");
//       return null;
//     });

// READ TASK to TASK PAGE

    get("/tasks/:id", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      model.put("task", task);
      model.put("template", "templates/task.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// UPDATE TASK TO TASK PAGE

    post("/tasks/:id/update", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      Task task = Task.find(Integer.parseInt(request.params("id")));
      String description = request.queryParams("description");
      task.update(description);
      response.redirect("/tasks/" + Integer.toString(task.getId()));
      return null;
    });

// READ ALL CATEGORIES LIST
    get("/categories", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      List<Category> categories = Category.all();
      model.put("categories", categories);
      model.put("template", "templates/categories.vtl");
      return new ModelAndView(model, layout);
    }, new VelocityTemplateEngine());

// CREATE CATEGORY TO CATEGORIES LIST

    post("/categories/create", (request, response) -> {
      HashMap<String, Object> model = new HashMap<String, Object>();
      String name = request.queryParams("name");
      Category newCategory = new Category(name);
      newCategory.save();
      response.redirect("/categories");
      return null;
    });

// DELETE CATEGORY TO CATEGORIES LIST

// READ CATEGORY PAGE

// UPDATE CATEGORY TO CATEGORY PAGE



  }
}
