package com;

import com.pages.*;
import com.tests.BaseTest;
import com.tests.RetryAnalyzer;
import com.tests.TestListener;
import com.utils.ConfigReader;
import com.google.gson.JsonObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.testng.annotations.Listeners;

import java.util.List;

@Listeners(TestListener.class)
public class E2EPurchaseTest extends BaseTest {

    @Test(priority = 1, retryAnalyzer = RetryAnalyzer.class)
    public void negativeLoginTest() throws InterruptedException {
        JsonObject cfg = ConfigReader.getConfig();
        LoginPage login = new LoginPage(driver);
        login.open();

        Thread.sleep(2000);

        login.login(
                cfg.getAsJsonObject("invalidUser").get("username").getAsString(),
                cfg.getAsJsonObject("invalidUser").get("password").getAsString()
        );

        Thread.sleep(2000);

        String err = login.getErrorMessage();
        Assert.assertTrue(
                err.toLowerCase().contains("username and password") ||
                        err.toLowerCase().contains("epic sadface"),
                "Error message not as expected: " + err
        );
    }

    @Test(priority = 2, retryAnalyzer = RetryAnalyzer.class)
    public void positiveE2EPurchaseFlow() throws InterruptedException {
        JsonObject cfg = ConfigReader.getConfig();
        LoginPage login = new LoginPage(driver);
        login.open();

        Thread.sleep(2000);

        login.login(
                cfg.getAsJsonObject("validUser").get("username").getAsString(),
                cfg.getAsJsonObject("validUser").get("password").getAsString()
        );

        Thread.sleep(2000);

        InventoryPage inventory = new InventoryPage(driver);
        inventory.sortLowToHigh();
        Thread.sleep(2000);
        inventory.addTwoCheapestToCart();

        Thread.sleep(2000);

        CartPage cart = new CartPage(driver);
        cart.openCart();
        Thread.sleep(2000);

        // sum prices
        List<?> varItems = cart.getCartItemsElements();
        double sum = 0;
        for (Object o : varItems) {
            org.openqa.selenium.WebElement we = (org.openqa.selenium.WebElement) o;
            String priceText = we.findElement(org.openqa.selenium.By.className("inventory_item_price"))
                    .getText().replace("$", "");
            sum += Double.parseDouble(priceText);
        }

        CheckoutPage checkout = new CheckoutPage(driver);
        checkout.proceedToCheckout();
        Thread.sleep(2000);

        checkout.fillCustomerInfo(
                cfg.getAsJsonObject("customer").get("firstName").getAsString(),
                cfg.getAsJsonObject("customer").get("lastName").getAsString(),
                cfg.getAsJsonObject("customer").get("postalCode").getAsString()
        );
        Thread.sleep(2000);

        double itemTotal = checkout.getItemTotal();
        double tax = checkout.getTax();
        double total = checkout.getTotal();

        checkout.finish();
        Thread.sleep(2000);

        ConfirmationPage conf = new ConfirmationPage(driver);
        String thank = conf.getThankYouMessage();
        Assert.assertTrue(
                thank.equalsIgnoreCase("THANK YOU FOR YOUR ORDER") ||
                        thank.equalsIgnoreCase("Thank you for your order!"),
                "Confirmation message mismatch: " + thank
        );
    }
}
