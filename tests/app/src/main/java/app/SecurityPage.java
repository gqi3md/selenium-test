package app;

import org.openqa.selenium.WebDriver;

public class SecurityPage extends UserPage{

	public SecurityPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Biztonság";
	}

}
