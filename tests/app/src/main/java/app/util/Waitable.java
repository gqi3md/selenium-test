package app.util;

import org.openqa.selenium.support.ui.WebDriverWait;

public class Waitable {
	protected WebDriverWait shortWait, mediumWait, longWait;
	
	protected WebDriverWait getWait(WaitInterval interval) {
		switch (interval) {
		case LONG:
			return longWait;
		case MEDIUM:
			return mediumWait;
		case SHORT:
			return shortWait;
		}
		return mediumWait;
	}
	
	protected void setWait(WaitInterval interval, WebDriverWait wait) {
		switch (interval) {
		case LONG:
			longWait = wait;
			break;
		case MEDIUM:
			mediumWait = wait;
			break;
		case SHORT:
			shortWait = wait;
			break;
		}
	}
	
	public WebDriverWait getWait() {
		return getWait(WaitInterval.MEDIUM);
	}
	
	public void copyWaits(Waitable o) {
		for(WaitInterval interval : WaitInterval.values())
			setWait(interval, o.getWait(interval));
	}
}
