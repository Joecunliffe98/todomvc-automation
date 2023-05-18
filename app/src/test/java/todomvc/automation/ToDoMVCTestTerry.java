package todomvc.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoMVCTestTerry{

    private static ChromeDriver driver;
    private String todoMVCHomePage = "https://todomvc.com";
    private String reactPage = "https://todomvc.com/examples/react/#/";

    public static void navigateToPage(String webpage){
        driver.get(webpage);
    }

    @BeforeAll
    static void launchBrowser(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testCanReachHomePage() {
        navigateToPage(todoMVCHomePage);
        System.out.println(driver.getTitle());
        assertEquals("TodoMVC", driver.getTitle());
    }

    @Test
    public void testCanReachReactApp(){
        navigateToPage(todoMVCHomePage);
        WebElement reactLink = driver.findElement(By.linkText("React"));
        reactLink.click();
        assertEquals("React", driver.getTitle().substring(0,5));
    }

    @Test
    public void testModifyATodoItemByDoubleClicking() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement todoItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        Actions action = new Actions(driver);
        action.doubleClick(todoItemText).perform();
        WebElement todoItemTextEdit = driver.findElement(By.cssSelector(".editing > .edit"));
        todoItemTextEdit.sendKeys(Keys.BACK_SPACE);
        todoItemTextEdit.sendKeys(Keys.ENTER);
        assertEquals("First Tod", todoItemText.getText());
    }

    @Test
    public void testEscapeModifyingATodoItem() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement todoItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        Actions action = new Actions(driver);
        action.doubleClick(todoItemText).perform();
        WebElement todoItemTextEdit = driver.findElement(By.cssSelector(".editing > .edit"));
        todoItemTextEdit.sendKeys("hfjhfusgvbmne");
        todoItemTextEdit.sendKeys(Keys.ESCAPE);
        assertEquals("First Todo", todoItemText.getText());
    }

    @Test
    public void testDeleteAnIncompleteTodoItem() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement todoItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        Actions action = new Actions(driver);
        WebElement deleteIcon = driver.findElement(By.cssSelector(".destroy"));
        // Move the cursor over the todo item so the delete icon is visible and class active
        action.moveToElement(todoItemText).perform();
        deleteIcon.click();
        assertEquals(0, driver.findElements(By.cssSelector("li:nth-child(1) label")).size());
    }

    @Test
    public void testDeleteACompletedToDoItem() throws InterruptedException {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle")).click();
        Thread.sleep(2000);
        WebElement itemTicked = driver.findElement(By.cssSelector(".todo-list")).findElement(By.cssSelector(".completed"));
        assertTrue(itemTicked.isDisplayed());
        WebElement todoItemText = driver.findElement(By.cssSelector("li:nth-child(1) label"));
        Actions action = new Actions(driver);
        WebElement deleteIcon = driver.findElement(By.cssSelector(".destroy"));
        // Move the cursor over the todo item so the delete icon is visible and class active
        action.moveToElement(todoItemText).perform();
        deleteIcon.click();
        assertEquals(0, driver.findElements(By.cssSelector("li:nth-child(1) label")).size());
    }

    @AfterAll
    static void closeBrowser(){
        driver.quit();
    }
}
