package org.selenium;

import org.selenium.pom.base.BaseTest;
import org.selenium.pom.objects.BillingAddress;
import org.selenium.pom.objects.Product;
import org.selenium.pom.objects.User;
import org.selenium.pom.pages.CartPage;
import org.selenium.pom.pages.CheckoutPage;
import org.selenium.pom.pages.HomePage;
import org.selenium.pom.pages.StorePage;
import org.selenium.pom.utils.JacksonUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

public class MyFirstTestCase extends BaseTest {

    @Test
    public void guestCheckoutUsingDirectBankTransfer() throws InterruptedException, IOException {
        // extract billing address and product test data from json
        BillingAddress billingAddress = JacksonUtils.deserialize("myBillingAddress.json", BillingAddress.class);
        Product product = new Product(1215);

        // use variables
        String searchTxt = "Blue";

        StorePage storePage = new HomePage(driver).
                load().
                goToStoreUsingMenu().
                search(searchTxt);
        Assert.assertEquals(storePage.getTitle(), "Search results: “" + searchTxt + "”");


        // TODO add functional method to add product to cart (clickAddToCartBtn,clickViewCartLnk)
        //  after handling synchronization
        storePage.clickAddToCartBtn(product.getName());
        Thread.sleep(5000);
        CartPage cartPage = storePage.clickViewCartLnk();
        Assert.assertEquals(cartPage.getProductName(),product.getName());

        // using the Builder pattern
        CheckoutPage checkoutPage = cartPage.
                checkout().
                enterBillingAddress(billingAddress);

        Thread.sleep(5000);
                checkoutPage.placeOrder();

        Thread.sleep(5000);
        Assert.assertEquals(checkoutPage.getNotice(),"Thank you. Your order has been received.");
    }


    @Test
    public void loginAndCheckoutUsingDirectBankTransfer() throws InterruptedException, IOException {
        // extract billing address and product test data from json
        BillingAddress billingAddress = JacksonUtils.deserialize("myBillingAddress.json", BillingAddress.class);
        Product product = new Product(1215);

        // we'll extract the single user from a config file  ---> then register a new user via API
        User user = new User("agatha", "Test@1Test");

        // use variables
        String searchTxt = "Blue";

        StorePage storePage = new HomePage(driver).
                load().
                goToStoreUsingMenu().
                search(searchTxt);
        Assert.assertEquals(storePage.getTitle(), "Search results: “" + searchTxt + "”");

        // TODO add functional method to add product to cart (clickAddToCartBtn,clickViewCartLnk)
        //  after handling synchronization
        storePage.clickAddToCartBtn(product.getName());
        Thread.sleep(5000);
        CartPage cartPage = storePage.clickViewCartLnk();
        Assert.assertEquals(cartPage.getProductName(),product.getName());

        CheckoutPage checkoutPage = cartPage.checkout();
        checkoutPage.clickHereToLogin();

        Thread.sleep(5000);
        checkoutPage.login(user);

        checkoutPage.
                enterBillingAddress(billingAddress);

        Thread.sleep(5000);
        checkoutPage.placeOrder();

        Thread.sleep(5000);
        Assert.assertEquals(checkoutPage.getNotice(),"Thank you. Your order has been received.");
    }
}