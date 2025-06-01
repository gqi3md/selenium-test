package app;

import java.net.MalformedURLException;
import java.net.URI;
import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import app.util.Conditions;
import app.util.WaitInterval;
import app.util.Waitable;

public abstract class BasePage extends Waitable{
	protected WebDriver driver;

	protected WebElement waitAndReturnElement(By locator, WaitInterval waitInterval) {
		getWait(waitInterval).until(Conditions.visibilityOfElementLocated(locator));
		return driver.findElement(locator);
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
	}

	protected void initWaitsAndAction() {
		this.shortWait = new WebDriverWait(driver, Duration.ofSeconds(5));
		this.mediumWait = new WebDriverWait(driver, Duration.ofSeconds(30));
		this.longWait = new WebDriverWait(driver, Duration.ofMinutes(2));
	}

	public BasePage(WebDriver driver) {
		this.driver = driver;
		this.driver.manage().window().maximize();
		initWaitsAndAction();
	}

	public BasePage(String URL) throws MalformedURLException {
		ChromeOptions options = new ChromeOptions();
		options.addArguments("−−lang=hu --no-sandbox");

		boolean couldStart = false;
		for(int i = 0; i < 3 && !couldStart; i++) {
			try {
				driver = new RemoteWebDriver(URI.create("http://selenium:4444/wd/hub").toURL(), (Capabilities) options);
				couldStart = true;
			} catch(SessionNotCreatedException e) {
				try {
					Thread.sleep(Duration.ofSeconds(30));
				} catch (InterruptedException e1) {
				}
			}
		}
		if(!couldStart)
			throw new SessionNotCreatedException("Could not start chrome session even after the third attempt!");

		initWaitsAndAction();

		driver.manage().window().maximize();
		driver.get(URL);
	}
	
	public WebDriver getDriver() {
		return driver;
	}

	protected abstract String getPageTitle();
}
