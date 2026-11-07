package base;

import config.Config;
import driver.DriverFactory;
import io.appium.java_client.android.AndroidDriver;
import listeners.FailureListener;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import pages.SettingsPage;

import java.lang.reflect.Method;
import java.util.logging.Logger;

@Listeners(FailureListener.class)
public abstract class BaseTest {
    private AndroidDriver driver;
    protected SettingsPage settingsPage;

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) throws Exception {
        Logger.getLogger(BaseTest.class.getName()).info("Starting " + method.getName());
        Config config = Config.load();
        driver = DriverFactory.create(config);
        settingsPage = new SettingsPage(driver, config.waitTimeout());
        settingsPage.openSettingsHome();
    }

    public AndroidDriver getDriver() {
        return driver;
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        if (driver != null) {
            try {
                driver.quit();
            } finally {
                driver = null;
            }
        }
    }
}

