package pages;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Map;
import java.util.logging.Logger;

public final class SettingsPage {
    public static final String PACKAGE = "com.android.settings";
    private static final By SEARCH_BUTTON = By.id("com.android.settings:id/search_action_bar");
    private static final By DISPLAY_TITLE = By.xpath(
            "//*[@resource-id='com.android.settings:id/collapsing_toolbar' and @content-desc='Display & touch']");
    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final Duration timeout;

    public SettingsPage(AndroidDriver driver, Duration timeout) {
        this.driver = driver;
        this.timeout = timeout;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public SettingsPage openSettingsHome() {
        Logger.getLogger(SettingsPage.class.getName()).info("Opening Settings with a fresh Activity task");
        // Use component, not positional intent: UiAutomator2 8.7.3 appends flags after
        // intent, and Android ignores options after that positional argument.
        // NEW_TASK | CLEAR_TASK resets navigation, not app data or system processes.
        driver.executeScript("mobile: startActivity", Map.of(
                "component", PACKAGE + "/.Settings", "action", "android.settings.SETTINGS",
                "flags", "0x10008000",
                "wait", true, "stop", false));
        return awaitHome();
    }

    public SettingsPage awaitHome() {
        isHomeVisible();
        return this;
    }

    public boolean isHomeVisible() {
        return wait.withMessage("Settings home must be foreground with its search bar")
                .until(ExpectedConditions.and(
                        d -> PACKAGE.equals(driver.getCurrentPackage()),
                        ExpectedConditions.visibilityOfElementLocated(SEARCH_BUTTON)));
    }

    public SettingsSearchPage openSearch() {
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON)).click();
        return new SettingsSearchPage(driver, timeout).awaitOpen();
    }

    public boolean isDisplayAndTouchVisible() {
        return wait.withMessage("Display & touch screen must be foreground")
                .until(ExpectedConditions.and(
                        d -> PACKAGE.equals(driver.getCurrentPackage()),
                        ExpectedConditions.visibilityOfElementLocated(DISPLAY_TITLE)));
    }
}
