package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.SettingsSearchPage;

public class FirstAndroidTest extends BaseTest {
    @Test
    public void shouldOpenAndroidSettings() {
        Assert.assertTrue(settingsPage.isHomeVisible(), "Settings home search bar must be visible");
        Assert.assertEquals(getDriver().getCurrentPackage(), "com.android.settings",
                "Android Settings must be foreground");
    }

    @Test
    public void shouldOpenSettingsSearch() {
        Assert.assertTrue(settingsPage.openSearch().isOpen(), "Settings search input must be visible");
    }

    @Test
    public void shouldSearchBluetoothInSettings() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Bluetooth");
        Assert.assertEquals(search.query(), "Bluetooth", "Search field must contain the complete query");
        Assert.assertTrue(search.isBluetoothResultVisible(), "Search results must include Bluetooth");
    }

    @Test
    public void shouldSearchDisplayInSettings() {
        SettingsSearchPage search = settingsPage.openSearch().searchFor("Display");
        Assert.assertEquals(search.query(), "Display", "Search field must contain the complete query");
        Assert.assertTrue(search.isDisplayResultVisible(), "Search results must include Display & touch");
    }
}
