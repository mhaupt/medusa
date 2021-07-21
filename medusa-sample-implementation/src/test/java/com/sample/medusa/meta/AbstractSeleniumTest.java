package com.sample.medusa.meta;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.web.server.LocalServerPort;

public class AbstractSeleniumTest {

    @LocalServerPort
    private int port;

    protected WebDriver driver;
    protected static String BASE;

    protected boolean isHeadless() {
        return true;
    }

    @BeforeAll
    public static void setupClass() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void setupTest() {
        BASE = "http://localhost:" + port;
        final ChromeOptions chromeOptions = new ChromeOptions();
        if (isHeadless()) {
            chromeOptions.addArguments("--no-sandbox");
            chromeOptions.addArguments("--headless");
            chromeOptions.addArguments("disable-gpu");
            chromeOptions.addArguments("window-size=1400,2100");
        }
        driver = new ChromeDriver(chromeOptions);
        logout();
    }

    @AfterEach
    public void teardown() {
        logout();
        if (driver != null) {
            driver.quit();
        }
    }

    protected void logout() {
        if (existsById("logout")) {
            clickById("logout");
        }
    }

    protected void login() {
        goTo("/login");
        if (!existsByCss("a[href='/map']")) {
            //fillFieldById("username", EMAIL_VALID);
            //fillFieldById("password", PASSW_VALID);
            clickById("login-button");
        }
        goTo("/debug-setup");
        goTo("/map");
    }

    protected void clickMenu(String menuItem) {
        clickById("menu-" + menuItem.toLowerCase());
        Assertions.assertTrue(getTextById("header").toLowerCase().contains(menuItem.toLowerCase()));
    }

    protected void goToLocation(String location) {
        goTo("/map");
        clickByCss("input[value='x']".replace("x", location));
    }

    protected void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (Exception ignored) {
        }
    }

    protected void clickByCss(String cssSelector) {
        driver.findElement(By.cssSelector(cssSelector)).click();
    }

    protected boolean existsByCss(String cssSelector) {
        return driver.findElements(By.cssSelector(cssSelector)).size() != 0;
    }

    protected boolean existsById(String id) {
        return driver.findElements(By.id(id)).size() != 0;
    }

    protected void goTo(String url) {
        driver.get(BASE + url);
    }

    protected String getTextById(String id) {
        return driver.findElement(By.id(id)).getText();
    }

    protected void fillFieldById(String id, String keys) {
        final WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(keys);
    }

    protected void clickById(String id) {
        driver.findElement(By.id(id)).click();
    }
}