package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;

import app.util.Conditions;
import app.util.WaitInterval;

@SuppressWarnings("deprecation")
public class PersonalInfoPageTest extends PageTest{
	PersonalInfoPage page;

	@BeforeEach
	public void open() {
		try {
			this.page = new LoginPage().loginUser().navigateToPersonalInfoPage();
		} catch (MalformedURLException e) {
			fail("Website could not be reached");
		}
		this.driver = page.getDriver();
		copyWaits(page);
	}

	@AfterEach
	public void close() {
		if(driver != null) {
			driver.quit();
		}
	}
	
	@ParameterizedTest
	@CsvSource(textBlock = """
			Név; Závogyán Dávid
			E-mail; gqi3mdsqattest@gmail.com
			Születésnap; 1920. június 4.
			Nem; Inkább nem adom meg
			""", delimiterString = "; ")
	public void testBasicPersonalData(String dataName, String dataContent) {
		String dataText = waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"%s\")]".formatted(dataName))).getAttribute("aria-label");
		assertEquals(dataContent.trim(), dataText.substring(dataName.length()).trim());
	}
	
	@ParameterizedTest
	@CsvSource(textBlock = """
			Név; https://myaccount.google.com/profile/name?continue
			E-mail; https://myaccount.google.com/email?continue
			Születésnap; https://myaccount.google.com/birthday?continue
			Nem; https://myaccount.google.com/gender?continue
			""", delimiterString = "; ")
	public void testSubpageNavigation(String dataName, String URL) {
		waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"%s\")]".formatted(dataName)), WaitInterval.MEDIUM).click();
		longWait.until(Conditions.urlContains(URL));
	}

	@Override
	protected BasePage getPage() {
		return page;
	}
}
