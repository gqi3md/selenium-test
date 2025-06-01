package app;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import app.util.WaitInterval;
import app.util.Waitable;

public abstract class PageTest extends Waitable{
	protected WebDriver driver;

	protected WebElement waitAndReturnElement(By locator, WaitInterval waitInterval) {
		try {
			getWait(waitInterval).until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch(TimeoutException e) {
			fail("The searched element does not exist:\n" + locator.toString());
		}

		WebElement element = driver.findElement(locator);
		try {
			getWait(waitInterval).until(ExpectedConditions.visibilityOf(element));
		} catch (TimeoutException e) {
			fail("The searched element is not visible:\n" + locator.toString());
		}

		return element;
	}

	protected boolean waitAndCheckElementVisibility(By locator, WaitInterval waitInterval) {
		try {
			getWait(waitInterval).until(ExpectedConditions.presenceOfElementLocated(locator));
		} catch(TimeoutException e) {
			return false;
		}
		WebElement element = driver.findElement(locator);
		return element.isDisplayed();
	}

	protected WebElement waitAndReturnElement(By locator) {
		return waitAndReturnElement(locator, WaitInterval.MEDIUM);
	}

	protected void attempt(Runnable action) {
		attempt(action, 3);
	}

	protected void attempt(Runnable action, int maxTries) {
		try {
			action.run();
		} catch(WebDriverException e) {
			if(e.getRawMessage().contains("Other element would receive the click: <div class=\"kPY6ve\"") || e.getRawMessage().contains("Remote browser did not respond to getCurrentUrl")){
				//The overlay div is shown when the page runs into an error (in my experience this is due to the limited resources when running many parallel tests)
				driver.navigate().refresh();
				attempt(action, 1, maxTries);
			} else throw e;
		}
	}

	protected void attempt(Runnable action, int currentTry, int maxTries) {
		if(currentTry < maxTries)
			try {
				action.run();
			} catch(WebDriverException e) {
				if(e.getRawMessage().contains("Other element would receive the click: <div class=\"kPY6ve\"") || e.getRawMessage().contains("Remote browser did not respond to getCurrentUrl")){
					//The overlay div is shown when the page runs into an error (in my experience this is due to the limited resources when running many parallel tests)
					driver.navigate().refresh();
					attempt(action, currentTry + 1, maxTries);
				} else throw e;
			}
		else
			fail("Failed even after multiple attempts");
	}
	
	protected abstract BasePage getPage();
	
	@Test
	public void testTitle() {
		assertEquals(getPage().getPageTitle(), driver.getTitle(), "Wrong page title: " + driver.getTitle());
	}
}
