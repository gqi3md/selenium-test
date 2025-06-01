package app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import app.util.Conditions;
import app.util.WaitInterval;

public class PersonalNameInfoPage extends UserPage {
	public PersonalNameInfoPage(WebDriver driver) {
		super(driver);
		//Wait until the loading overlay disappears
		WebElement headerTitle = waitAndReturnElement(By.xpath(".//h1[contains(text(),'Név')]"), WaitInterval.MEDIUM);
		mediumWait.until(Conditions.elementToBeClickable(headerTitle));
		longWait.until((d) -> d.findElements(By.xpath(".//div[contains(concat(\" \",normalize-space(@class),\" \"),\" KL4X6e \")][contains(concat(\" \",normalize-space(@class),\" \"),\" TuA45b \")]")).size() == 0);
	}
	
	public ChangeNicknamePage navigateToChangeNickName() {
		waitAndReturnElement(By.xpath(".//a[starts-with(@href,\"%s\")]".formatted("profile/nickname/edit")), WaitInterval.MEDIUM).click();
		longWait.until(Conditions.urlContains("https://myaccount.google.com/profile/nickname/edit"));

		return new ChangeNicknamePage(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Név";
	}
}
