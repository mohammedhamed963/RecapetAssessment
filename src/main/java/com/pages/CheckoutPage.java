package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage {
    private WebDriver driver;

    private By checkoutButton = By.id("checkout");
    private By firstName = By.id("first-name");
    private By lastName = By.id("last-name");
    private By postalCode = By.id("postal-code");
    private By continueButton = By.id("continue");
    private By finishButton = By.id("finish");

    private By itemTotal = By.className("summary_subtotal_label");
    private By tax = By.className("summary_tax_label");
    private By total = By.className("summary_total_label");

    public CheckoutPage(WebDriver driver) {
        this.driver = driver;
    }

    public void proceedToCheckout() {
        driver.findElement(checkoutButton).click();
    }

    public void fillCustomerInfo(String fName, String lName, String zip) {
        driver.findElement(firstName).sendKeys(fName);
        driver.findElement(lastName).sendKeys(lName);
        driver.findElement(postalCode).sendKeys(zip);
        driver.findElement(continueButton).click();
    }

    public double getItemTotal() {
        String txt = driver.findElement(itemTotal).getText().replace("Item total: $", "");
        return Double.parseDouble(txt);
    }

    public double getTax() {
        String txt = driver.findElement(tax).getText().replace("Tax: $", "");
        return Double.parseDouble(txt);
    }

    public double getTotal() {
        String txt = driver.findElement(total).getText().replace("Total: $", "");
        return Double.parseDouble(txt);
    }

    public void finish() {
        driver.findElement(finishButton).click();
    }
}
