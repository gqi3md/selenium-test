package app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import app.util.Conditions;
import app.util.WaitInterval;

public abstract class UserPage extends BasePage {

	public UserPage(WebDriver driver) {
		super(driver);
		waitForPageToLoad();
	}
	
	protected void waitForPageToLoad() {
		//Wait until the loading overlay disappears
		WebElement headerTitle = waitAndReturnElement(By.xpath(".//h1[contains(text(),'%s')]".formatted(getPageTitle())), WaitInterval.MEDIUM);
		longWait.until(Conditions.elementToBeClickable(headerTitle));
		longWait.until((d) -> d.findElements(By.xpath(".//div[contains(concat(\" \",normalize-space(@class),\" \"),\" KL4X6e \")][contains(concat(\" \",normalize-space(@class),\" \"),\" TuA45b \")]")).size() == 0);
	}
}

