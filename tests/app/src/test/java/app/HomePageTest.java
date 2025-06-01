package app;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import app.util.Conditions;
import app.util.WaitInterval;

@SuppressWarnings("deprecation")
public class HomePageTest extends PageTest{

	HomePage homePage;

	@BeforeEach
	public void open() {
		try {
			this.homePage = new LoginPage().loginUser();
		} catch (MalformedURLException e) {
			fail("Website could not be reached");
		}
		this.driver = homePage.getDriver();
		copyWaits(homePage);
	}

	@AfterEach
	public void close() {
		if(driver != null) {
			driver.quit();
		}
	}

	@Test
	public void testLogout() {
		try {
			waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"Google-fiók: Závogyán Dávid\")]")).click();
		} catch (TimeoutException e) {
			fail("The profile menu button is missing");
		}

		try {
			WebElement iFrame = waitAndReturnElement(By.xpath(".//iframe[@name=\"account\"]"));
			driver.switchTo().frame(iFrame);
		} catch (TimeoutException e) {
			fail("The profile card did not appear");
		}

		try {
			waitAndReturnElement(By.xpath(".//a[starts-with(@href,\"https://accounts.google.com/Logout\")]")).click();
		} catch (TimeoutException e) {
			fail("The logout button is missing");
		}

		longWait.until(Conditions.urlContains("https://accounts.google.com/v3/signin"));
	}

	@Test
	public void testRelog() {
		LoginPage loginPage = homePage.logout();
		try {
			waitAndReturnElement(By.xpath(".//div[starts-with(@data-identifier,\"" + LoginPage.correctUsername + "\")]"));
		} catch (TimeoutException e) {
			fail("The selectable username is missing");
		}

		homePage = loginPage.loginUser();
		try {
			waitAndReturnElement(By.xpath(".//a[starts-with(@aria-label,\"Google-fiók: Závogyán Dávid\")]"));
		} catch (TimeoutException e) {
			fail("The profile menu button is missing");
		}
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			.//header[@role="banner"], true
			.//div[count(.//header[not(self::node()[@role="banner"])]) > 0], false
			""", delimiterString = ", ")
	public void testSearchBar(String selectorPrefix, boolean hasButton) {

		if(hasButton) {
			waitAndReturnElement(By.xpath("%s//button[@aria-label=\"Keresés\"]/span".formatted(selectorPrefix)), WaitInterval.SHORT).click();
		}

		WebElement searchField = waitAndReturnElement(By.xpath("%s//input[starts-with(@aria-label,\"Keresés a Google\")]".formatted(selectorPrefix)), WaitInterval.SHORT);
		searchField.click();
		searchField.sendKeys("jelszó bizt");
		assertEquals("jelszó bizt", searchField.getAttribute("value"), "The search term does not match the typed value!");

		WebElement listBox = waitAndReturnElement(By.xpath("%s//div[starts-with(@data-placeholder,\"Keresés a Google\")]/following-sibling::div/following-sibling::div/div[@role=\"listbox\"]".formatted(selectorPrefix)), WaitInterval.SHORT);
		assertEquals("3", listBox.getAttribute("data-childcount"), "The number of the search results must be exactly 3!");

		//For some reason, the selenium driver thinks that the options are not displayed
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		assertTrue(
				(boolean) executor.executeScript("return window.getComputedStyle(document.evaluate('%s//div[starts-with(@data-placeholder,\"Keresés a Google\")]/following-sibling::div/following-sibling::div/div[@role=\"listbox\"]/div/div[@role=\"option\"]//a', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue) !== 'none'".formatted(selectorPrefix), new Object[] {}),
				"The options are not visible!"
				);
		executor.executeScript("document.evaluate('%s//div[starts-with(@data-placeholder,\"Keresés a Google\")]/following-sibling::div/following-sibling::div/div[@role=\"listbox\"]/div/div[@role=\"option\"]//a', document, null, XPathResult.FIRST_ORDERED_NODE_TYPE, null).singleNodeValue.click()".formatted(selectorPrefix), new Object[] {});

		mediumWait.until(ExpectedConditions.or(
				Conditions.urlContains("https://myaccount.google.com/signinoptions/password"),
				Conditions.urlContains("/signin/challenge")
				));
	}

	@Test
	public void testNav() {
		mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]")));
		assertEquals(7, driver.findElements(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a")).size(), "The number of shown nav-links must be exactly 7!");
		assertAll(
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"./\"]")).isDisplayed(),
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"personal-info\"]")).isDisplayed(),
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"data-and-privacy\"]")).isDisplayed(),
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"security\"]")).isDisplayed(),
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[@href=\"people-and-sharing\"]")).isDisplayed(),
				() -> driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[starts-with(@href,\"https://www.google.com/account/about/?hl=hu\")]")).isDisplayed()
		);
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			./; https://myaccount.google.com/
			personal-info; https://myaccount.google.com/personal-info
			data-and-privacy; https://myaccount.google.com/data-and-privacy
			security; https://myaccount.google.com/security
			people-and-sharing; https://myaccount.google.com/people-and-sharing
			payments-and-subscriptions; https://myaccount.google.com/payments-and-subscriptions
			""", delimiterString = "; ")
	public void testNavLinks(String hrefPrefix, String URL) {
		mediumWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]")));
		driver.findElement(By.xpath(".//nav[contains(concat(\" \",normalize-space(@class),\" \"),\" ky9loc \")]/ul/li/a[starts-with(@href,\"%s\")]".formatted(hrefPrefix))).click();

		mediumWait.until(Conditions.urlToBe(URL));
	}

	@ParameterizedTest
	@CsvSource(textBlock = """
			https://myaccount.google.com/
			https://myaccount.google.com/data-and-privacy
			https://myaccount.google.com/security
			https://myaccount.google.com/people-and-sharing
			https://myaccount.google.com/payments-and-subscriptions
			https://myaccount.google.com/security-checkup/1?continue=https%3A%2F%2Fmyaccount.google.com%2F
			https://mail.google.com/mail/u/0/#inbox
			https://www.google.com/maps?authuser=0
			https://calendar.google.com/calendar/u/0/r?pli=1
			https://play.google.com/store/games?device=windows&pli=1
			https://passwords.google.com/?utm_source=OGB&utm_medium=AL&pli=1
			""")
	public void testReachableSites(String URL) {
		driver.get(URL);
		longWait.until(Conditions.urlToBe(URL));
	}

	@Override
	protected BasePage getPage() {
		return homePage;
	}
}
