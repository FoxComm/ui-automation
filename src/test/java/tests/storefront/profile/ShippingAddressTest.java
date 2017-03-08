package tests.storefront.profile;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.yandex.qatools.allure.annotations.Description;
import pages.StorefrontPage;
import testdata.DataProvider;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.refresh;

public class ShippingAddressTest extends DataProvider {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl + "/" + storefrontCategory, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }


    @Test(priority = 1)
    @Description("Add a new shipping address at /profile")
    public void createShipAddress_profile() throws IOException {
        provideTestData("a customer signed up on storefront");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickAddAddressBtn();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879766");
        p.cityFld().shouldHave(value("Seattle"));
        p.clickSaveBtn();

        p.myAddresses().shouldHaveSize(1);
    }

    @Test(priority = 2)
    @Description("Edit existing shipping address at /profile")
    public void editShipAddress_profile() throws IOException {
        provideTestData("a storefront signed up customer with a shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickEditBtn_address("1");
        p.setName_shipAddress("Edited Name");
        p.setZip("90210");
        p.cityFld().shouldHave(value("Beverly Hills"));
        p.clickSaveBtn();

        p.shipAddress_name("1").shouldHave(text("Edited Name"));
        p.shipAddress_zip("1").shouldHave(text("90210"));
        p.shipAddress_state("1").shouldHave(text("California"));
    }

    @Test(priority = 3)
    @Description("Delete a shipping address at /profile")
    public void deleteShipAddress_profile() throws IOException {
        provideTestData("a storefront signed up customer with 2 shipping addresses");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickRemoveBtn_address("1");
        refresh();

        p.myAddresses().shouldHaveSize(1);
    }

    @Test(priority = 4)
    @Description("Flag existing shipping address as a default at /profile")
    public void setShipAddressAsDefault_profile() throws IOException {
        provideTestData("a storefront signed up customer with 2 shipping addresses");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickEditBtn_address("2");
        p.clickDefaultChbx();
        p.clickSaveBtn();

        p.assertAddressIsDefault("2");
    }

    @Test(priority = 5)
    @Description("Restore a just deleted shipping address")
    public void restoreShipAddress() throws IOException {
        provideTestData("a storefront signed up customer with a shipping address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickRemoveBtn_address("1");
        p.clickRestoreBtn_address("1");

        p.assertAddressRestored("1");
        p.restoreAddressBtn("1").shouldNotBe(visible);
    }

    @Test(priority = 6)
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

    @Test(priority = 7)
    @Description("Create new shipping address at Checkout page")
    public void editShipAddress_checkout() throws IOException {
        provideTestData("a storefront signed up customer with a shipping address and a product in cart");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.openCart();
        p.clickCheckoutBtn_cart();
        p.clickEditAddressBtn_checkout("1");
        p.setName_shipAddress("Edited Name");
        p.setZip("90210");
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

    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}