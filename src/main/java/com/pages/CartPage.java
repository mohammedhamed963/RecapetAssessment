package com.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage {
    private WebDriver driver;
    private By cartLink = By.className("shopping_cart_link");
    private By cartItems = By.className("cart_item");

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public void openCart() {
        driver.findElement(cartLink).click();
    }

    public int getCartItemsCount() {
        return driver.findElements(cartItems).size();
    }

    public String[] getItemNamePriceByIndex(int index) {
        WebElement item = driver.findElements(cartItems).get(index);
        String name = item.findElement(By.className("inventory_item_name")).getText();
        String price = item.findElement(By.className("inventory_item_price")).getText();
        return new String[]{name, price};
    }

    public List<WebElement> getCartItemsElements() {
        return driver.findElements(cartItems);
    }
}
