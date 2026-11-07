package driver;

import config.Config;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import java.net.MalformedURLException;
import java.time.Duration;
import java.util.logging.Logger;

public final class DriverFactory {
    private DriverFactory() { }

    public static AndroidDriver create(Config config) throws MalformedURLException {
        UiAutomator2Options options = new UiAutomator2Options()
                .setDeviceName(config.deviceName())
                .setUdid(config.udid())
                .setNoReset(true)
                .setNewCommandTimeout(config.commandTimeout())
                .setAdbExecTimeout(config.adbTimeout());
        AndroidDriver driver = new AndroidDriver(config.appiumUrl().toURL(), options);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ZERO);
            Logger.getLogger(DriverFactory.class.getName()).info(
                    "Session " + driver.getSessionId() + ", device " + config.udid());
            return driver;
        } catch (RuntimeException error) {
            try {
                driver.quit();
            } catch (RuntimeException cleanup) {
                error.addSuppressed(cleanup);
            }
            throw error;
        }
    }
}

