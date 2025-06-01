package app;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

public class SecurityPageTest extends PageTest{
	SecurityPage page;
	
	@BeforeEach
	public void open() {
		try {
			this.page = new LoginPage()
							.loginUser()
							.navigateToSecurityPage();
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

	@Override
	protected BasePage getPage() {
		return page;
	}
}
