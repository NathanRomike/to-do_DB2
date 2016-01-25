import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

import org.sql2o.*;

public class Category {
  private int id;
  private String name;

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Category(String name) {
    this.name = name;
  }

  public static List<Category> all() {
    String sql = "SELECT id, name FROM categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  /* tells .equals() method to treat Category class objects from the database as
  if they occupy the the same place in memory. the first part of the if-else
  tells it to ignore rule if we are comparing objects of different classes */
  @Override
  public boolean equals(Object otherCategory) {
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM categories where id=:id";
      Category category = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
      return category;
    }
  }

  public void addTask(Task task) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO categories_tasks (category_id, task_id) VALUES (:category_id, :task_id)";
      con.createQuery(sql)
        .addParameter("category_id", this.getId())
        .addParameter("task_id", task.getId())
        .executeUpdate();
    }
  }

  public ArrayList<Task> getTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT task_id FROM categories_tasks WHERE category_id = :category_id";
      List<Integer> taskIds = con.createQuery(sql)
                                 .addParameter("category_id", this.getId())
                                 .executeAndFetch(Integer.class);
      ArrayList<Task> tasks = new ArrayList<Task>();

      for (Integer taskId : taskIds) {
        String taskQuery = "SELECT * FROM tasks WHERE id = :taskId";
        Task task = con.createQuery(taskQuery)
                       .addParameter("taskId", taskId)
                       .executeAndFetchFirst(Task.class);
        tasks.add(task);
      }
      return tasks;
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
      String deleteFromTasksCategoryQuery = "DELETE FROM categories_tasks WHERE category_id = :categoryId";
      con.createQuery(deleteFromTasksCategoryQuery)
         .addParameter("categoryId", this.getId())
         .executeUpdate();

      String deleteFromCategoriesQuery = "DELETE FROM categories WHERE id = :id;";
      con.createQuery(deleteFromCategoriesQuery)
         .addParameter("id", id)
         .executeUpdate();
    }
  }

}
