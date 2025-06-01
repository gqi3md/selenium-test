package app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import app.util.Conditions;

public class ChangeNicknamePageTest extends PageTest{
	ChangeNicknamePage page;
	
	@BeforeEach
	public void open() {
		try {
			this.page = new LoginPage()
							.loginUser()
							.navigateToPersonalInfoPage()
							.navigateToNameInfoPage()
							.navigateToChangeNickName();
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
	
	@Test
	public void testNameChange() {
		//Must be done sequentially, since one change would affect another read
		Random rng = ThreadLocalRandom.current();
		for(int i = 0; i < 5; i++){
			String newName = IntStream.range(0, rng.nextInt(5, 16))
								.mapToObj(n -> (char)('A' + rng.nextInt(0, 26)))
								.reduce(new StringBuilder(), StringBuilder::append, StringBuilder::append)
								.toString();
			
			WebElement input = waitAndReturnElement(By.xpath(".//input[preceding-sibling::span[child::span[child::span[contains(text(),'Becenév')]]]]"));
			input.clear();
			input.sendKeys(newName);
			waitAndReturnElement(By.xpath(".//button[child::span[contains(text(), 'Mentés')]]")).click();
			
			mediumWait.until(Conditions.urlContains("https://myaccount.google.com/profile/name?continue"));
			assertEquals(newName, waitAndReturnElement(By.xpath(".//div[preceding-sibling::div[contains(text(),'Becenév')]]")).getText());
			
			this.page = new PersonalNameInfoPage(driver).navigateToChangeNickName();
		}
	}

	@Override
	protected BasePage getPage() {
		return page;
	}
}
