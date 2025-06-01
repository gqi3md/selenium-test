package app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import app.util.Conditions;
import app.util.WaitInterval;

public class PersonalInfoPage extends UserPage{

	public PersonalInfoPage(WebDriver driver) {
		super(driver);
	}

	public PersonalNameInfoPage navigateToNameInfoPage() {
		waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"%s\")]".formatted("Név")), WaitInterval.MEDIUM).click();
		longWait.until(Conditions.urlContains("https://myaccount.google.com/profile/name?continue"));

		return new PersonalNameInfoPage(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Személyes adatok";
	}
}
