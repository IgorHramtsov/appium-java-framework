package tests;

import base.BaseTest;
import config.Config;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.SettingsSearchPage;

public class SettingsNavigationTest extends BaseTest {
    @Test
    public void shouldReturnHomeFromSearchWithBack() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Bluetooth");
        Assert.assertEquals(search.query(), "Bluetooth", "Query must exist before navigating back");
        Assert.assertTrue(search.backToSettings().isHomeVisible(), "Back must return to Settings home");
    }

    @Test
    public void shouldReplaceSearchQuery() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Bluetooth");
        Assert.assertEquals(search.query(), "Bluetooth", "Initial query must be entered");
        search.searchFor("Display");
        Assert.assertEquals(search.query(), "Display", "New query must replace the old query");
    }

    @Test
    public void shouldClearSearchQuery() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Bluetooth");
        Assert.assertEquals(search.query(), "Bluetooth", "Query must exist before clearing");
        search.clearQuery();
        Assert.assertEquals(search.query(), "", "Clear must leave an empty search field");
        Assert.assertTrue(search.isOpen(), "Clearing must keep the search screen open");
    }

    @Test
    public void shouldRestoreSettingsAfterHomeAndForeground() {
        getDriver().pressKey(new KeyEvent(AndroidKey.HOME));
        new WebDriverWait(getDriver(), Config.load().waitTimeout())
                .withMessage("Home must move Settings out of foreground")
                .until(d -> !"com.android.settings".equals(getDriver().getCurrentPackage()));
        Assert.assertNotEquals(getDriver().getCurrentPackage(), "com.android.settings", "Home must move Settings out of foreground");
        getDriver().activateApp("com.android.settings");
        Assert.assertTrue(settingsPage.isHomeVisible(), "Settings home must return after activation");
    }

    @Test
    public void shouldRelaunchHomeAfterSearch() {
        Assert.assertTrue(settingsPage.openSearch().searchFor("Bluetooth").isOpen(), "Search must be open before relaunch");
        settingsPage.openSettingsHome();
        Assert.assertTrue(settingsPage.isHomeVisible(), "Explicit relaunch must reset navigation to home");
        Assert.assertTrue(settingsPage.openSearch().isOpen(), "Search must work after relaunch");
    }

    @Test
    public void shouldOpenDisplaySettingsFromSearch() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Display");
        Assert.assertEquals(search.query(), "Display", "Search field must contain the complete query");
        Assert.assertTrue(search.openDisplaySettings().isDisplayAndTouchVisible(),
                "Display & touch screen must be opened");
    }
}

