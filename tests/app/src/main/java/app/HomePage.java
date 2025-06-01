package app;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import app.util.Conditions;
import app.util.WaitInterval;

public class HomePage extends BasePage{

	public HomePage(WebDriver driver) {
		super(driver);
	}
	
	public LoginPage logout() {
		waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"Google-fiók: Závogyán Dávid\")]")).click();
		driver.switchTo().frame(waitAndReturnElement(By.xpath(".//iframe[@name=\"account\"]")));
		waitAndReturnElement(By.xpath(".//a[starts-with(@href,\"https://accounts.google.com/Logout\")]")).click();
		longWait.until(Conditions.urlContains("https://accounts.google.com/v3/signin"));
		
		return new LoginPage(driver);
	}
	
	public PersonalInfoPage navigateToPersonalInfoPage() {
		waitAndReturnElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"personal-info\"]"), WaitInterval.MEDIUM).click();
		longWait.until(Conditions.urlContains("https://myaccount.google.com/personal-info"));
		
		return new PersonalInfoPage(driver);
	}
	
	public SecurityPage navigateToSecurityPage() {
		waitAndReturnElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"security\"]"), WaitInterval.MEDIUM).click();
		longWait.until(Conditions.urlContains("https://myaccount.google.com/security"));
		
		return new SecurityPage(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Google-fiók";
	}
}
