package todomvc.automation;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.swing.*;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoMVCTestTerry{

    private static ChromeDriver driver;
    private String todoMVCHomePage = "https://todomvc.com";
    private String reactPage = "https://todomvc.com/examples/react/#/";

    public static void navigateToPage(String webpage){
        driver.get(webpage);
    }

    @BeforeEach
    public void launchBrowser(){
        WebDriverManager.chromedriver().setup();
        driver = new ChromeDriver();
    }

    @Test
    public void testCanReachHomePage() {
        navigateToPage(todoMVCHomePage);
        assertEquals("TodoMVC", driver.getTitle());
    }

    @Test
    public void testCanReachReactApp(){
        navigateToPage(todoMVCHomePage);
        WebElement reactLink = driver.findElement(By.linkText("React"));
        reactLink.click();
        assertEquals("React", driver.getTitle().substring(0,5));
    }

    // MODIFYING TODO ITEMS
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

    // MOVING & REMOVING A TODO ITEM
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
    public void testDeleteACompletedToDoItem() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        driver.findElement(By.cssSelector(".toggle")).click();
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

    // STATUS BAR ACTIONS
    @Test
    public void testStatusBarShowsZeroItemsLeftWhenAddedAndDeleted() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("2 items left", itemsRemainingText.getText());
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement firstItemToClick = toggleItems.get(1);
        WebElement secondItemToClick = toggleItems.get(0);
        firstItemToClick.click();
        secondItemToClick.click();
        assertEquals("0 items left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarShowsOneItemLeftWhenOnlyAdded() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("1 item left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarShowsOneItemLeftWhenAddedAndDeleted() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Third Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("3 items left", itemsRemainingText.getText());
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement firstItemToClick = toggleItems.get(1);
        WebElement secondItemToClick = toggleItems.get(2);
        firstItemToClick.click();
        secondItemToClick.click();
        assertEquals("1 item left", itemsRemainingText.getText());
    }
    @Test
    public void testStatusBarShowsTwoItemsLeftWhenOnlyAdded() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("2 items left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarShowsTwoItemsLeftWhenAddedAndDeleted() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Third Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("3 items left", itemsRemainingText.getText());
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement itemToClick = toggleItems.get(1);
        itemToClick.click();
        assertEquals("2 items left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarShowsNinetyNineItemsLeftWhenOnlyAdded() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        for (int i = 1; i < 100; i++){
            todoInput.sendKeys(i +" Todo");
            todoInput.sendKeys(Keys.ENTER);
        }
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("99 items left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarShowsNinetyNineItemsLeftAddedAndDeleted() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        for (int i = 1; i < 101; i++){
            todoInput.sendKeys(i +" Todo");
            todoInput.sendKeys(Keys.ENTER);
        }
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        assertEquals("100 items left", itemsRemainingText.getText());
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement itemToClick = toggleItems.get(1);
        itemToClick.click();
        assertEquals("99 items left", itemsRemainingText.getText());
    }

    @Test
    public void testStatusBarHiddenWhenNoItemsLeftAddedDeletedAndCleared() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Third Todo");
        todoInput.sendKeys(Keys.ENTER);
        WebElement itemsRemainingText = driver.findElement(By.cssSelector(".todo-count"));
        WebElement statusBar = driver.findElement(By.cssSelector(".footer"));
        assertEquals("3 items left", itemsRemainingText.getText());
        assertTrue(statusBar.isDisplayed());
        assertEquals(1, driver.findElements(By.cssSelector(".footer")).size());
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement firstItemToClick = toggleItems.get(0);
        WebElement secondItemToClick = toggleItems.get(1);
        WebElement thirdItemToClick = toggleItems.get(2);
        firstItemToClick.click();
        secondItemToClick.click();
        thirdItemToClick.click();
        assertEquals("0 items left", itemsRemainingText.getText());
        driver.findElement(By.cssSelector(".clear-completed")).click();
        assertEquals(0, driver.findElements(By.cssSelector(".footer")).size());
    }

    @Test
    public void testStatusBarHiddenWhenInitiallyLoaded() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        assertEquals(0, driver.findElements(By.cssSelector(".footer")).size());
    }

    @Test
    public void testStatusBarActiveAllCompletedBtnsToggle() {
        navigateToPage(reactPage);
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".new-todo")));
        WebElement todoInput = driver.findElement(By.cssSelector(".new-todo"));
        todoInput.sendKeys("First Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Second Todo");
        todoInput.sendKeys(Keys.ENTER);
        todoInput.sendKeys("Third Todo");
        todoInput.sendKeys(Keys.ENTER);
        List<WebElement> listItems = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(3, listItems.size());
        Integer itemsCompleted = 0;
        Integer itemsActive = 0;
        List<WebElement> toggleItems = driver.findElements(By.cssSelector(".toggle"));
        WebElement itemToClick = toggleItems.get(0);
        itemToClick.click();
        for (int i = 0; i < listItems.size(); i++){
            String listItemClassName = listItems.get(i).getAttribute("class");
            if (listItemClassName.equals("completed")) {
                itemsCompleted++;
            } else {
                itemsActive++;
            }
        }
        driver.findElement(By.linkText("Active")).click();
        List<WebElement> activeListItems = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(itemsActive, activeListItems.size());
        driver.findElement(By.linkText("Completed")).click();
        List<WebElement> completedListItems = driver.findElements(By.cssSelector(".todo-list li"));
        assertEquals(itemsCompleted, completedListItems.size());
        driver.findElement(By.linkText("All")).click();
        assertEquals(itemsActive + itemsCompleted, listItems.size());
    }

    @AfterEach
    public void closeBrowser(){
        driver.quit();
    }
}
