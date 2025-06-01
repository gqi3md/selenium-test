package app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import app.util.Conditions;
import app.util.Config;

public class LoginPasswordPage extends BasePage{

	public static final String correctPassword = Config.instance().correctPassword;

	public LoginPasswordPage(WebDriver driver) {
		super(driver);
	}

	public HomePage loginUser() {
		attempt(() -> {
			WebElement nextButton = waitAndReturnElement(By.xpath(".//*[@id=\"passwordNext\"]"));
			mediumWait.until(Conditions.elementToBeClickable(nextButton));

			WebElement passwordField = waitAndReturnElement(By.xpath(".//*[@id=\"password\"]//input[@type=\"password\"]"));
			passwordField.sendKeys(LoginPasswordPage.correctPassword);

			nextButton.click();
		}, 5);

		longWait.until(Conditions.urlContains("https://myaccount.google.com"));
		return new HomePage(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Bejelentkezés – Google-fiók";
	}
}
