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
    System.out.println(Integer.toString(myTask.getId()));
    goTo("http://localhost:4567/tasks/" + Integer.toString(myTask.getId()));
    fill("#description").with("Mow the backyard");
    submit(".btn-warning");
    assertThat(pageSource()).contains("Mow the backyard");
  }
// As a user, I want to create new lists of different categories so that I can keep similar tasks together (phone calls, school work, house work, errands to run, bills to pay, etc)
  // @Test
  // public void multipleCategoriesAreCreated() {
  //   Category myCategory = new Category("Household chores");
  //   myCategory.save();
  //   Category mySecondCategory = new Category("Grocery shopping");
  //   mySecondCategory.save();
  //   goTo("http://localhost:4567/categories");
  //   assertThat(pageSource()).contains("Household chores");
  //   assertThat(pageSource()).contains("Grocery shopping");
  // }

// As a user, I want to select a single list and see the tasks for it.
  // @Test
  // public void taskIsCreatedTest() {
  //   goTo("http://localhost:4567/");
  //   click("a", withText("Add a New Category"));
  //   fill("#name").with("Household chores");
  //   submit(".btn");
  //   click("a", withText("Household chores"));
  //   click("a", withText("Add a new task"));
  //   fill("#description").with("Sweep floor");
  //   submit(".btn");
  //   assertThat(pageSource()).contains("Sweep floor");
  // }

}
