package com.willowtreeapps;

        import org.junit.Assert;
        import org.openqa.selenium.By;
        import org.openqa.selenium.WebDriver;
        import org.openqa.selenium.WebElement;
        import org.openqa.selenium.support.ui.ExpectedConditions;
        import org.openqa.selenium.support.ui.WebDriverWait;
        import java.util.Random;
        import java.util.List;

/**
 * Created on 5/23/17.
 */
public class HomePage extends BasePage {


    public HomePage(WebDriver driver) {
        super(driver);
    }
    WebDriverWait wait = new WebDriverWait(driver, 10);
    
    public List<WebElement> photos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo"))); //abstracted so that getRandomPhoto and validateRandomSelectionStats can use the same photo list

    public WebElement getCorrectPhoto(){
        sleep(4000);
        List <WebElement> Photos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
        WebElement correct = Photos.stream().filter((element) -> element.getText().contains((driver.findElement(By.id("name"))).getText())).findFirst().orElse(null);
        return correct; //always selects the correct photo by filtering the photos list to the photo that matches given name
    }
    public WebElement getIncorrectPhoto(){
        sleep(4000);
        List <WebElement> Photos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
        WebElement incorrect = Photos.stream().filter((element) -> !element.getText().contains((driver.findElement(By.id("name"))).getText())).findFirst().orElse(null);
        return incorrect; //the exact opposite of getCorrectPhoto
    }
    public WebElement getRandomPhoto(){
        sleep(4000);
        if (photos.isEmpty()){
                photos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
            }
        Random randomizer = new Random();
        WebElement rPhoto  = photos.get(randomizer.nextInt(photos.size())); //selects random photo from the list
        photos.remove(photos.indexOf(rPhoto)); //removes the selected photo from the list so that it cannot be selected again
        return rPhoto;
    }

    public void validateTitleIsPresent() {
        WebElement title = driver.findElement(By.cssSelector("h1"));
        Assert.assertTrue(title != null);
    }


    public void validateClickingFirstPhotoIncreasesTriesCounter() {
        //Wait for page to load
        sleep(6000);

        int count = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        driver.findElement(By.className("photo")).click();

        sleep(6000);

        int countAfter = Integer.parseInt(driver.findElement(By.className("attempts")).getText());

        Assert.assertTrue(countAfter > count);

    }
    public void validateClickingCorrectPhotoIncreasesStreak(){

        int beforeStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        getCorrectPhoto().click();
       int afterStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertTrue(afterStreak == beforeStreak+1);
         beforeStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        getCorrectPhoto().click();
        afterStreak = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertTrue(afterStreak == beforeStreak+1);
    }

    public void validateClickingIncorrectPhotoClearsStreak () {
        getCorrectPhoto().click();
        getCorrectPhoto().click();
        int streakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertEquals(2, streakCount);
        getIncorrectPhoto().click();
        streakCount = Integer.parseInt(driver.findElement(By.className("streak")).getText());
        Assert.assertEquals(0, streakCount);
    }
    public void validateRandomSelectionStats(int runs) {
        int count = 0;
        int correct = 0;
        while (count < runs) {

            WebElement choice = getRandomPhoto();
            choice.click();
            if (choice.getText().contains(driver.findElement(By.id("name")).getText())) { //compares the random choice to the given name to determine if it is correct
                sleep(5000);
                correct++;//counts number of correct answers selected
                photos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo"))); //gets a new fresh photo list on selection of a correct answer as the photos are now different
            }
            count++;
        }        
        sleep(4000);
        Assert.assertEquals(correct, Integer.parseInt(driver.findElement(By.id("stats")).findElement(By.className("correct")).getText())); //compares the correct count to the correct stat shown
        Assert.assertEquals(count, Integer.parseInt(driver.findElement(By.className("attempts")).getText())); //compares the tries stat to the number of iterations of the loop
    }
    public void validatePhotosAndNameChangeOnCorrect(){
        String Person = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText();
        List <WebElement>  Photos =wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
        getCorrectPhoto().click();
        sleep(4000);
        String newPerson = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText();
        List <WebElement> newPhotos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
        Assert.assertNotEquals(Photos,newPhotos);
        Assert.assertNotEquals(Person, newPerson);

    }
    public void validateMissedNamesAppearMoreOften (int runs)//I figured that if i missed the first person, and then tracked that person over the next x amount of names given, the name should appear more than if I tracked the occurrences of a name that I answered correctly on over the same number of runs but I didn't find that to be the case. I need  more information on the requirements for this feature and how it was implemented
    {
        int count = 0;
        int firstRun = 0;
        int secondRun= 0;
        List <WebElement> firstPhotos = wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(By.className("photo")));
        String tracker = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText();
        getIncorrectPhoto().click();
        getCorrectPhoto().click();

        while (count < runs){
            //sleep(3000);
            if (wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText() == tracker) {
                getIncorrectPhoto().click();
                getCorrectPhoto().click();
                firstRun++;
            }
            else{
                getCorrectPhoto().click();
            }
            sleep(5000);

        }
        count = 0;
        tracker = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText();
        getCorrectPhoto().click();

        while (count < runs) {
            //sleep(3000);
            if (wait.until(ExpectedConditions.presenceOfElementLocated(By.id("name"))).getText() == tracker)
            {
                secondRun++;
            }
        getCorrectPhoto().click();
            sleep(5000);

        }
        Assert.assertTrue(firstRun > secondRun);
    }
}
