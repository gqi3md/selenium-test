package app;

import org.openqa.selenium.WebDriver;

public class PeopleAndSharingPage extends UserPage{

	public PeopleAndSharingPage(WebDriver driver) {
		super(driver);
	}

	@Override
	protected String getPageTitle() {
		return "Személyek és megosztás";
	}


}
