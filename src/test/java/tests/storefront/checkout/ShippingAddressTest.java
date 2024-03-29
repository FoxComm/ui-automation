package tests.storefront.checkout;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import base.StorefrontTPGBasePage;
import ru.yandex.qatools.allure.annotations.Description;
import ru.yandex.qatools.allure.annotations.Features;
import ru.yandex.qatools.allure.annotations.Severity;
import ru.yandex.qatools.allure.annotations.Stories;
import ru.yandex.qatools.allure.model.SeverityLevel;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.*;

public class ShippingAddressTest extends Preconditions {

    private StorefrontTPGBasePage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        clearCache();
    }

    @Test(priority = 1)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Address")
    @Description("Can create new shipping address at Checkout page")
    public void createShipAddress_checkout() throws IOException {
        provideTestData("a storefront signed up customer with a product in cart");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879766");
        p.clickSaveAddressBtn();
        p.shipAddress_checkout().shouldBe(visible);
        scrollPageUp();
        p.clickLogo();
        p.openProfile();

        p.myAddresses().shouldHaveSize(1);
    }

    @Test(priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Address")
    @Description("Can edit shipping address at Checkout page")
    public void editShipAddress_checkout() throws IOException {
        provideTestData("a storefront signed up customer with a shipping address and a product in cart");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
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
        p.openProfile();
        p.shipAddress_name("1").shouldHave(text("Edited Name"));
        p.shipAddress_zip("1").shouldHave(text("90210"));
        p.shipAddress_state("1").shouldHave(text("California"));
    }

    @Test(priority = 3)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Address")
    @Description("A default shipping address is pre-selected once customer gets onto checkout page")
    public void defaultShipAddressIsAutoselectedOnCheckout() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, has default address");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickContinueBtn();

        p.name_appliedShipAddress().shouldHave(text("Default Address"));
    }

    @Test(priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Address")
    @Description("A just added shipping address should get auto-selected (not the first created shipping address, no default ship address)")
    public void newShipAddressAutoselected_noDefault() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, NO default address");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickAddAddressBtn();
        p.fillOutAddressForm("New Address", "665 Clinton Lane", "Suit 42", "18702", "7417417417");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        shouldBeVisible(p.shipAddressByName_checkout("New Address"), "New shipping address isn't visible");

        p.name_appliedShipAddress().shouldHave(text("New Address"));
    }

    @Test(priority = 5)
    @Severity(SeverityLevel.CRITICAL)
    @Features("Storefront-TPG")
    @Stories("Checkout : Ship Address")
    @Description("A just added shipping address should get auto-selected; not first created shipping address, has default ship address")
    public void newShipAddressAutoselected_hasDefault() throws IOException {
        provideTestData("a storefront signed up customer, a cart with 1 product, 2 shipping addresses, HAS default address");

        p = openPage(storefrontUrl, StorefrontTPGBasePage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickAddAddressBtn();
        p.fillOutAddressForm("New Address", "665 Clinton Lane", "Suit 42", "18702", "7417417417");
        p.clickSaveAddressBtn();
        p.clickContinueBtn();
        shouldBeVisible(p.shipAddressByName_checkout("New Address"), "New shipping address isn't visible");

        p.name_appliedShipAddress().shouldHave(text("New Address"));
    }

}
