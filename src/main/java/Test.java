import org.openqa.selenium.*;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.io.File;
import java.util.List;

public class Test {
    private static final String driverPass = "src/main/resources/webdrivers/";
    File iEdriver = new File(driverPass + "IEDriverServer.exe");
    private WebDriver ied;
    private WebDriverWait waiting;
    private static final String url = "http://www.sberbank.ru/ru/person";
    private static final String place = "Нижегородская область";
    private static final String placeLocator = "//DIV[@class = 'sbrf-div-list-inner --area bp-area header_contacts']/DIV[@class = 'bp-widget bp-ui-dragRoot']/DIV[@class = 'bp-widget-body']/DIV[1]/DIV[@class = 'region-list']";

    @BeforeSuite
    public void start() {
        System.setProperty("webdriver.ie.driver", iEdriver.getAbsolutePath());
        ied = new InternetExplorerDriver();
        waiting = new WebDriverWait(ied, 30);
        ied.get(url);
        ied.manage().window().maximize();
    }

    @org.testng.annotations.Test
    public void middle() {
        ied.findElement(By.xpath(placeLocator)).click();
        waiting.ignoring(NoSuchElementException.class)
                .ignoring(StaleElementReferenceException.class)
                .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//H4[@class = 'region-list__modal-caption']")));
        ied.findElement(By.xpath("//INPUT[@placeholder = 'Введите название региона']")).sendKeys(place);
        ied.findElement(By.xpath("//U[contains(text(), 'Нижегородская область')]")).click();
        waiting.ignoring(NoSuchElementException.class)
               .ignoring(StaleElementReferenceException.class)
               .until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//DIV[@class = 'sbrf-div-list-inner --area bp-area header_contacts']/DIV[@class = 'bp-widget bp-ui-dragRoot']/DIV[@class = 'bp-widget-body']/DIV[1]/DIV[@class = 'region-list']/A[@title = 'Нижегородская область']/SPAN[@class = 'region-list__name']")));
        Assert.assertEquals(place, ied.findElement(By.xpath(placeLocator)).getText(),"Регион не совпадает");
        ((JavascriptExecutor) ied).executeScript("arguments[0].scrollIntoView(true);", ied.findElement(By.xpath("//DIV[@class = 'footer-info']")));
        List<WebElement> socialList = ied.findElements(By.xpath("//UL[@class = 'social__list']/LI"));
        for (int i=0; i <=socialList.size()-1; i++) {
            Assert.assertTrue(socialList.get(i).isDisplayed());
        }
    }

    @AfterSuite
    public void finish() {
        ied.quit();
        ied = null;
    }


}
