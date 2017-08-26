package by.itransition.selenium.logic;

import by.itransition.selenium.entity.User;
import org.ajbrown.namemachine.Name;
import org.ajbrown.namemachine.NameGenerator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class AutoRegistration {

    private static final String DEV_BY_REGISTRATION_URL = "https://dev.by/registration";
    private static final String MAILINATOR_COM_URL = "https://www.mailinator.com/";
    private static final String DRIVER_URL = "C:\\Users\\user\\IdeaProjects\\Selenium\\driver\\chromedriver.exe";
    private static final String DRIVER_NAME = "webdriver.chrome.driver";
    private static final String OUTPUT = "./files/output.txt";

    private static final String ELEMENT_ID_USERNAME = "user_username";
    private static final String ELEMENT_ID_EMAIL = "user_email";
    private static final String ELEMENT_ID_PASSWORD = "user_password";
    private static final String ELEMENT_ID_CONFIRM_PASSWORD = "user_password_confirmation";
    private static final String ELEMENT_ID_FIRST_NAME = "user_first_name";
    private static final String ELEMENT_ID_LAST_NAME = "user_last_name";
    private static final String ELEMENT_ID_USER_AGREEMENT = "user_agreement";
    private static final String ELEMENT_NAME_COMMIT = "commit";
    private static final String ELEMENT_ID_INFOBOX = "inboxfield";
    private static final String ELEMENT_TEXT_CONFIRM = "подтвердить";
    private static final String MSG_FRAME_ID = "msg_body";

    private static final String MAILINATOR = "@mailinator.com";
    private static final String DEV_BY_NAME = "Dev.by";

    public static void register() {
        System.setProperty(DRIVER_NAME, DRIVER_URL);
        WebDriver driver = new ChromeDriver();
        User user = generateUser();
        registerDevBy(user, driver);
        confirmMailinator(user, driver);
        writeToFile(user);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            System.err.println("InterruptedException in register");
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void registerDevBy(User user, WebDriver driver) {
        driver.navigate().to(DEV_BY_REGISTRATION_URL);
        driver.findElement(By.id(ELEMENT_ID_USERNAME)).sendKeys(user.getUsername());
        driver.findElement(By.id(ELEMENT_ID_EMAIL)).sendKeys(user.getEmail());
        driver.findElement(By.id(ELEMENT_ID_PASSWORD)).sendKeys(user.getPassword());
        driver.findElement(By.id(ELEMENT_ID_CONFIRM_PASSWORD)).sendKeys(user.getPassword());
        driver.findElement(By.id(ELEMENT_ID_FIRST_NAME)).sendKeys(user.getFirstName());
        driver.findElement(By.id(ELEMENT_ID_LAST_NAME)).sendKeys(user.getLastName());
        driver.findElement(By.id(ELEMENT_ID_USER_AGREEMENT)).click();
        driver.findElement(By.name(ELEMENT_NAME_COMMIT)).click();
    }

    private static void confirmMailinator(User user, WebDriver driver) {
        driver.navigate().to(MAILINATOR_COM_URL);
        driver.findElement(By.id(ELEMENT_ID_INFOBOX)).sendKeys(user.getEmail());
        driver.findElements(By.tagName("button")).get(1).click();
        for (WebElement el : driver.findElements(By.tagName("li"))) {
            if (el.getText().contains(DEV_BY_NAME)) {
                el.click();
                break;
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("InterruptedException in confirmMailinator");
            e.printStackTrace();
        }
        driver.switchTo().frame(MSG_FRAME_ID);
        driver.findElement(By.linkText(ELEMENT_TEXT_CONFIRM)).click();
    }

    private static User generateUser() {
        SecureRandom random = new SecureRandom();
        Name name = new NameGenerator().generateName();
        String username = name.getFirstName() + (random.nextInt() % (int) Math.pow(10, 9 - name.getFirstName().length()));
        String email = username + MAILINATOR;
        String password = name.getFirstName() + (random.nextInt() % (int) Math.pow(10, 9 - name.getFirstName().length()));
        return new User(username, email, password, name.getFirstName(), name.getLastName());
    }

    private static void writeToFile(User user) {
        FileWriter fw;
        try {
            fw = new FileWriter(OUTPUT);
            fw.write("username: " + user.getUsername() + "\n");
            fw.write("email: " + user.getEmail() + "\n");
            fw.write("password: " + user.getPassword() + "\n");
            fw.write("firstName: " + user.getFirstName() + "\n");
            fw.write("lastName: " + user.getLastName() + "\n");
            fw.close();
        } catch (IOException e) {
            System.err.println("IOException in writeToFile");
            e.printStackTrace();
        }
    }

}