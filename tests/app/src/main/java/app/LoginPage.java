package app;

import java.net.MalformedURLException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import app.util.Conditions;
import app.util.Config;
import app.util.WaitInterval;

public class LoginPage extends BasePage{

	public static final String URL = Config.instance().BaseURL;
	public static final String correctUsername = Config.instance().correctUsername;

	public LoginPage(WebDriver driver) {
		super(driver);
	}

	public LoginPage() throws MalformedURLException {
		super(URL);
	}

	protected LoginPasswordPage selectUser() {
		waitAndReturnElement(By.xpath(".//div[starts-with(@data-identifier,\"" + correctUsername + "\")]")).click();
		longWait.until(Conditions.urlContains("/signin/challenge"));
		return new LoginPasswordPage(driver);
	}

	protected LoginPasswordPage enterUser() {
		WebElement usernameField = waitAndReturnElement(By.id("identifierId"));
		usernameField.sendKeys(LoginPage.correctUsername);

		WebElement nextButton = waitAndReturnElement(By.xpath(".//*[@id=\"identifierNext\"]//button[@type=\"button\"]"));
		nextButton.click();

		longWait.until(Conditions.urlContains("/signin/challenge"));
		return new LoginPasswordPage(driver);
	}

	public HomePage loginUser() {
		waitAndReturnElement(By.id("headingText"), WaitInterval.LONG);
		if(driver.findElements(By.id("identifierId")).size() > 0) {
			return enterUser().loginUser();
		} else {
			return selectUser().loginUser();
		}
	}

	@Override
	protected String getPageTitle() {
		return "Sign in - Google Accounts";
	}
}
