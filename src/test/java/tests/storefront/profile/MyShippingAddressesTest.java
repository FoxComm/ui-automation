package tests.storefront.profile;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pages.storefront.StorefrontPage;
import ru.yandex.qatools.allure.annotations.Description;
import testdata.Preconditions;

import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.refresh;

public class MyShippingAddressesTest extends Preconditions {

    private StorefrontPage p;

    @BeforeMethod(alwaysRun = true)
    public void cleanUp_before() {
        p = openPage(storefrontUrl, StorefrontPage.class);
        p.cleanUp_beforeMethod();
    }

    @Test(priority = 1)
    @Description("Can add a new shipping address")
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
        p.setZIP("90210");
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

    @Test(priority = 4)
    @Description("Flag existing shipping address as a default at /profile")
    public void setShipAddressAsDefault_existing() throws IOException {
        provideTestData("a storefront signed up customer with 2 shipping addresses");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickEditBtn_address("2");
        p.clickDefaultChbx();
        p.clickSaveBtn();

        p.assertAddressIsSelected("2");
    }

    @Test(priority = 6)
    @Description("Can make new shipping address a default; customer doesn't have a default address")
    public void setShipAddressAsDefault_new_noDefault() throws IOException {
        provideTestData("a storefront signed up customer with 2 shipping addresses");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickAddAddressBtn();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879766");
        p.clickDefaultChbx();
        p.clickSaveBtn();

        p.assertAddressIsSelected("3");
    }
    
    @Test(priority = 7)
    @Description("Can make new shipping address a default; customer has a default address")
    public void setShipAddressAsDefault_new_hasDefault() throws IOException {
        provideTestData("a storefront signed up customer with 2 shipping addresses, has default address");

        p = openPage(storefrontUrl, StorefrontPage.class);
        p.logIn(customerEmail, "78qa22!#");
        p.selectInUserMenu("PROFILE");
        p.clickAddAddressBtn();
        p.fillOutAddressForm("John Doe", "7500 Roosevelt Way NE", "Block 42", "98115", "9879879766");
        p.clickDefaultChbx();
        p.clickSaveBtn();

        p.assertAddressIsSelected("3");
    }



    @AfterMethod(alwaysRun = true)
    public void cleanUp_after() {
        p.cleanUp_afterMethod();
    }

}
