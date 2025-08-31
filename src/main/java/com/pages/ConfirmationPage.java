package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage {
    private WebDriver driver;
    private By thankYouMessage = By.className("complete-header");

    public ConfirmationPage(WebDriver driver) {
        this.driver = driver;
    }

    public String getThankYouMessage() {
        return driver.findElement(thankYouMessage).getText();
    }
}
