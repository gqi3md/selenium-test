package app.util;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;

/*
 * Had to rewrite some of the ExpectedConditions due to the browser freezing
 * @see ExpectedConditions
 */
public class Conditions {
	public static ExpectedCondition<Boolean> urlContains(final String fraction) {
		return new ExpectedCondition<Boolean>() {
			private String currentUrl = "";

			@Override
			public Boolean apply(WebDriver driver) {
				try {
					currentUrl = driver.getCurrentUrl();
				} catch (Exception e)
				{
					return false;
				}

				return currentUrl != null && currentUrl.contains(fraction);
			}

			@Override
			public String toString() {
				return String.format("url to contain \"%s\". Current url: \"%s\"", fraction, currentUrl);
			}
		};
	}
	
	  public static ExpectedCondition<Boolean> urlToBe(final String url) {
		    return new ExpectedCondition<Boolean>() {
		      private String currentUrl = "";

		      @Override
		      public Boolean apply(WebDriver driver) {
		    	  try {
		    		  currentUrl = driver.getCurrentUrl();
		    	  } catch(Exception e) {
		    		  return false;
		    	  }
		       
		        return currentUrl != null && currentUrl.equals(url);
		      }

		      @Override
		      public String toString() {
		        return String.format("url to be \"%s\". Current url: \"%s\"", url, currentUrl);
		      }
		    };
		  }

	private static WebElement elementIfVisible(WebElement element) {
		return element.isDisplayed() ? element : null;
	}

	public static ExpectedCondition<WebElement> visibilityOfElementLocated(final By locator) {
		return new ExpectedCondition<WebElement>() {
			@Override
			public WebElement apply(WebDriver driver) {
				try {
					return elementIfVisible(driver.findElement(locator));
				} catch (Exception e) {
					return null;
				}
			}

			@Override
			public String toString() {
				return "visibility of element located by " + locator;
			}
		};
	}
	
	public static ExpectedCondition<WebElement> elementToBeClickable(final WebElement element) {
	    return new ExpectedCondition<WebElement>() {

	      @Override
	      public WebElement apply(WebDriver driver) {
	        WebElement visibleElement = ExpectedConditions.visibilityOf(element).apply(driver);
	        try {
	          if (visibleElement != null && visibleElement.isEnabled()) {
	            return visibleElement;
	          }
	          return null;
	        } catch (Exception e) {
	          return null;
	        }
	      }

	      @Override
	      public String toString() {
	        return "element to be clickable: " + element;
	      }
	    };
	  }
}
