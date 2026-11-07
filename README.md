# Appium Java Framework

Small mobile automation project for testing Android Settings.

## Tech Stack

- Java 21
- Appium + UiAutomator2
- Selenium
- TestNG
- Maven
- Page Object Model

## How to Run

1. Start the Android emulator.
2. Start the Appium server:

   ```bash
   appium
   ```

3. Check the device connection:

   ```bash
   adb devices
   ```

4. Update `src/test/resources/config.properties` if needed.
5. Run the tests:

   ```bash
   mvn clean test
   ```

To run a specific test class:

```bash
mvn test -Dtest=FirstAndroidTest
```

Test reports are saved in `target/surefire-reports`.

Screenshots and XML files for failed tests are saved in `target/artifacts`.