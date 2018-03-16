package com.willowtreeapps;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebTest {

    private WebDriver driver;

    /**
     * Change the prop if you are on Windows or Linux to the corresponding file type
     * The chrome WebDrivers are included on the root of this project, to get the
     * latest versions go to https://sites.google.com/a/chromium.org/chromedriver/downloads
     */
    @Before
    public void setup() {
        System.setProperty("webdriver.chrome.driver", "chromedriver.exe");
        Capabilities capabilities = DesiredCapabilities.chrome();
        driver = new ChromeDriver(capabilities);
        driver.navigate().to("http://www.ericrochester.com/name-game/");
    }

    @Test
    public void test_validate_title_is_present() {
        new HomePage(driver)
                .validateTitleIsPresent();
    }

    @Test
    public void test_clicking_photo_increases_tries_counter() {
        new HomePage(driver)
                .validateClickingFirstPhotoIncreasesTriesCounter();
    }

    @Test
    public void test_counts_work_on_correct(){
        new HomePage(driver)
                .validateClickingCorrectPhotoIncreasesStreak();
    }

    @Test
    public void test_incorrect_clears_Streak(){
        new HomePage(driver)
                .validateClickingIncorrectPhotoClearsStreak();
    }

    @Test
    public void test_counters_correct_after_random_tries(){
        new HomePage(driver)
                .validateRandomSelectionStats(10);
    }

    @Test
    public void test_name_and_photos_change_when_answer_is_correct() {
        new HomePage(driver)
                .validatePhotosAndNameChangeOnCorrect();
    }
    /*@Test
    public void test_inoorrect_answeres_occur_more_often(){
        new HomePage(driver)
                .validateMissedNamesAppearMoreOften(50);
    }*/


    @After
    public void teardown() {
        driver.quit();
        System.clearProperty("webdriver.chrome.driver");
    }

}
