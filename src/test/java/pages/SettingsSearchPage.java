package pages;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public final class SettingsSearchPage {
    private static final By INPUT = By.id("com.google.android.settings.intelligence:id/open_search_view_edit_text");
    // Confirmed in the API 37 emulator hierarchy; excludes the input and recent query title.
    private static final By BLUETOOTH_RESULT = By.xpath(
            "//*[@resource-id='com.google.android.settings.intelligence:id/list_results']"
                    + "//*[@clickable='true'][.//*[@resource-id='android:id/title' and @text='Bluetooth']]");
    private static final By DISPLAY_RESULT = By.xpath(
            "//*[@resource-id='com.google.android.settings.intelligence:id/list_results']"
                    + "//*[@clickable='true'][.//*[@resource-id='android:id/title' and @text='Display & touch']]");
    private final AndroidDriver driver;
    private final WebDriverWait wait;
    private final Duration timeout;

    public SettingsSearchPage(AndroidDriver driver, Duration timeout) {
        this.driver = driver;
        this.timeout = timeout;
        this.wait = new WebDriverWait(driver, timeout);
    }

    public SettingsSearchPage awaitOpen() {
        isOpen();
        return this;
    }

    public boolean isOpen() {
        return wait.withMessage("Settings search input must be visible")
                .until(ExpectedConditions.visibilityOfElementLocated(INPUT)).isDisplayed();
    }

    public SettingsSearchPage searchFor(String query) {
        Logger.getLogger(SettingsSearchPage.class.getName()).info("Entering search query: " + query);
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(INPUT));
        input.clear();
        input.sendKeys(query);
        return this;
    }

    public String query() {
        WebElement input = wait.until(ExpectedConditions.visibilityOfElementLocated(INPUT));
        // On API 37 Accessibility exposes the placeholder as text when the field is empty.
        // showing-hint is supported by UiAutomator2 on API 26+; do not compare English hints.
        return Boolean.parseBoolean(input.getAttribute("showing-hint")) ? "" : input.getText();
    }

    public boolean isBluetoothResultVisible() {
        return wait.withMessage("Bluetooth must appear in Settings search results")
                .until(ExpectedConditions.visibilityOfElementLocated(BLUETOOTH_RESULT)).isDisplayed();
    }

    public boolean isDisplayResultVisible() {
        return wait.withMessage("Display & touch must appear in Settings search results")
                .until(ExpectedConditions.visibilityOfElementLocated(DISPLAY_RESULT)).isDisplayed();
    }

    public SettingsPage openDisplaySettings() {
        wait.until(ExpectedConditions.elementToBeClickable(DISPLAY_RESULT)).click();
        return new SettingsPage(driver, timeout);
    }

    public SettingsSearchPage clearQuery() {
        wait.until(ExpectedConditions.elementToBeClickable(INPUT)).clear();
        return this;
    }

    public SettingsPage backToSettings() {
        // The first Back can dismiss the IME instead of leaving the search Activity.
        if (driver.isKeyboardShown()) driver.hideKeyboard();
        driver.pressKey(new KeyEvent(AndroidKey.BACK));
        return new SettingsPage(driver, timeout).awaitHome();
    }
}
