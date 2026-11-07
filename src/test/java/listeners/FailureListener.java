package listeners;

import base.BaseTest;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestResult;
import org.testng.Reporter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Capture before @AfterMethod, including setup failures when a session exists. */
public final class FailureListener implements IInvokedMethodListener {
    private static final Logger LOG = Logger.getLogger(FailureListener.class.getName());

    @Override
    public void afterInvocation(IInvokedMethod method, ITestResult result) {
        if (result.getStatus() != ITestResult.FAILURE || !(result.getInstance() instanceof BaseTest test)) return;
        AndroidDriver driver = test.getDriver();
        LOG.log(Level.SEVERE, "Failed: " + result.getName(), result.getThrowable());
        if (driver == null || driver.getSessionId() == null) return;
        Path directory = Path.of("target", "artifacts");
        String name = result.getTestClass().getRealClass().getSimpleName() + "-" + result.getName()
                + "-" + UUID.randomUUID();
        try {
            Files.createDirectories(directory);
        } catch (Exception error) {
            LOG.log(Level.WARNING, "Cannot create artifacts directory", error);
            return;
        }
        try {
            Path screenshot = directory.resolve(name + ".png");
            Files.write(screenshot, driver.getScreenshotAs(OutputType.BYTES));
            Reporter.log("Failure screenshot: " + screenshot.toAbsolutePath(), true);
        } catch (Exception error) {
            LOG.log(Level.WARNING, "Cannot capture screenshot", error);
        }
        try {
            Path hierarchy = directory.resolve(name + ".xml");
            Files.writeString(hierarchy, driver.getPageSource());
            Reporter.log("Failure UI hierarchy: " + hierarchy.toAbsolutePath(), true);
        } catch (Exception error) {
            LOG.log(Level.WARNING, "Cannot capture UI hierarchy", error);
        }
    }
}

