package tests.storefront.checkout;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;

public class ShippingAddressTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Create new shipping address at Checkout page")
    public void createShipAddress_checkout() throws IOException {
        provideTestData("a storefront signed up customer with a product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879766");
        p.clickSaveAddressBtn();

        p.shipAddress_checkout().shouldBe(visible);
        scrollPageUp();
        p.clickLogo();
        p.selectInUserMenu("PROFILE");
        p.myAddresses().shouldHaveSize(1);
    }

    @Test(priority = 2)
    @Description("Create new shipping address at Checkout page")
    public void editShipAddress_checkout() throws IOException {
        provideTestData("a storefront signed up customer with a shipping address and a product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.editAddress_checkout("1");
        p.setName_shipAddress("Edited Name");
        p.setZIP("90210");
        p.cityFld().shouldHave(value("Beverly Hills"));
        p.clickSaveAddressBtn();

        p.shipAddress_name("1").shouldHave(text("Edited Name"));
        p.shipAddress_zip("1").shouldHave(text("90210"));
        p.shipAddress_state("1").shouldHave(text("California"));
        scrollPageUp();
        p.clickLogo();
        p.selectInUserMenu("PROFILE");
        p.shipAddress_name("1").shouldHave(text("Edited Name"));
        p.shipAddress_zip("1").shouldHave(text("90210"));
        p.shipAddress_state("1").shouldHave(text("California"));
    }

    @Test(priority = 3)
    @Description("A default shipping address is pre-selected once customer gets onto checkout page")
    public void defaultShipAddressIsAutoselectedOnCheckout() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, has default address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();

        p.appliedShipAddress_name().shouldHave(text("Default Address"));
    }

    @Test(priority = 4)
    @Description("A just added shipping address will get auto-selected; not first created shipping address, NO DEFAULT")
    public void newShipAddressAutoselected_noDefault() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, NO default address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickAddAddressBtn();
        p.fillOutAddressForm("New Address", "665 Clinton Lane", "Suit 42", "18702", "7417417417");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        shouldBeVisible(p.shipAddressByName_checkout("New Address"), "New shipping address isn't visible");

        p.appliedShipAddress_name().shouldHave(text("New Address"));
    }

    @Test(priority = 5)
    @Description("A just added shipping address will get auto-selected; not first created shipping address, HAS DEFAULT")
    public void newShipAddressAutoselected_hasDefault() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, HAS default address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickAddAddressBtn();
        p.fillOutAddressForm("New Address", "665 Clinton Lane", "Suit 42", "18702", "7417417417");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        shouldBeVisible(p.shipAddressByName_checkout("New Address"), "New shipping address isn't visible");

        p.appliedShipAddress_name().shouldHave(text("New Address"));
    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
