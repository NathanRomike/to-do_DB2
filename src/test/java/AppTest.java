import org.fluentlenium.adapter.FluentTest;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import static org.junit.Assert.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import static org.assertj.core.api.Assertions.assertThat;
import static org.fluentlenium.assertj.FluentLeniumAssertions.assertThat;
import static org.fluentlenium.core.filter.FilterConstructor.*;


public class AppTest extends FluentTest {
  public WebDriver webDriver = new HtmlUnitDriver();

  @Override
  public WebDriver getDefaultDriver() {
    return webDriver;
  }

  @Rule
  public DatabaseRule database = new DatabaseRule();

  @ClassRule
  public static ServerRule server = new ServerRule();

// As a user, I want to see a welcome page that includes where I can go and what I can do.
  @Test
    public void rootTest() {
      goTo("http://localhost:4567/tasks");
      assertThat(pageSource()).contains("Welcome");
    }

// As a user, I want to see all of the tasks that I have created so that I can manage them one at a time.
  @Test
  public void taskIsCreatedTest() {
    goTo("http://localhost:4567/tasks");
    fill("#description").with("Do the dishes");
    submit(".btn-primary");
    assertThat(pageSource()).contains("Do the dishes");
  }

  @Test
  public void taskIsUpdatedTest() {
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    Category myCategory = new Category("Household chores");
    myCategory.save();
    goTo("http://localhost:4567/tasks/" + Integer.toString(myTask.getId()));
    fill("#description").with("Mow the backyard");
    submit(".btn-warning");
    assertThat(pageSource()).contains("Mow the backyard");
  }

  @Test
  public void categoryIsCreated() {
    goTo("http://localhost:4567/categories");
    fill("#name").with("Yardwork");
    submit(".btn-success");
    assertThat(pageSource()).contains("Yardwork");
  }

  @Test
  public void categoriesAreUpdated() {
    Category myCategory = new Category("Yardwork");
    myCategory.save();
    Task myTask = new Task("Mow the lawn");
    myTask.save();
    goTo("http://localhost:4567/categories/" + Integer.toString(myCategory.getId()));
    fill("#name").with("Home chores");
    submit(".btn-warning");
    assertThat(pageSource()).contains("Home chores");
  }

  @Test
  public void taskAddedToCategory() {
    Category myCategory = new Category("Yardwork");
    myCategory.save();
    Task myTask = new Task("Rake leaves");
    myTask.save();
    goTo("http://localhost:4567/categories/" + Integer.toString(myCategory.getId()));
    click("option", withText("Rake leaves"));
    submit(".btn-success");
    assertThat(pageSource()).contains("Rake leaves");
  }

}
