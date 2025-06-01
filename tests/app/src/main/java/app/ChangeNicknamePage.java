package app;

import org.openqa.selenium.WebDriver;

public class ChangeNicknamePage extends UserPage{
	public ChangeNicknamePage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Becenév";
	}
}
