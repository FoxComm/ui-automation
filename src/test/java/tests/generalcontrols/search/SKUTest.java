package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.SkusPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class SKUTest extends DataProvider {

    private SkusPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {

       open(adminUrl);
        if ((Objects.equals(getUrl(), adminUrl + "/login"))) {
            LoginPage loginPage = openPage(adminUrl + "/login", LoginPage.class);
            loginPage.login("tenant", "admin@admin.com", "password");
            loginPage.userMenuBtn().shouldBe(visible);
        }
        provideTestData("SKU for search tests");
        p = openPage(adminUrl + "/skus", SkusPage.class);

    }

    @Test(priority = 1)
    public void skuCode() {

        p.addFilter("SKU : Code", sku);
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU with requested code."));

    }

    @Test(priority = 2)
    public void skuTitle() {

        p.addFilter("SKU : Title", skuTitle);
        p.itemOnList(skuTitle).shouldBe(visible
                .because("Search has failed to find a SKU with requested code."));

    }

    //--------------- PRICE
    //---- '='

    @Test(priority = 3)
    public void skuPrice_equals_lowerPrice() {

        p.addFilter("SKU : Price", "=", "899.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 4)
    public void skuPrice_equals_exactPrice() {

        p.addFilter("SKU : Price", "=", "900.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    @Test(priority = 5)
    public void skuPrice_equals_higherPrice() {

        p.addFilter("SKU : Price", "=", "999.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    //---- '<>'

    @Test(priority = 6)
    public void skuPrice_notEquals_lowerPrice() {

        p.addFilter("SKU : Price", "<>", "899.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    @Test(priority = 7)
    public void skuPrice_notEquals_exactPrice() {

        p.addFilter("SKU : Price", "<>", "900.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 8)
    public void skuPrice_notEquals_higherPrice() {

        p.addFilter("SKU : Price", "<>", "999.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    //---- '>'

    @Test(priority = 9)
    public void skuPrice_moreThan_lowerPrice() {

        p.addFilter("SKU : Price", ">", "999.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    @Test(priority = 10)
    public void skuPrice_moreThan_exactPrice() {

        p.addFilter("SKU : Price", ">", "900.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 11)
    public void skuPrice_moreThan_higherPrice() {

        p.addFilter("SKU : Price", ">", "999.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    //---- '>='
    @Test(priority = 12)
    public void skuPrice_moreOrEquals_lowerPrice() {

        p.addFilter("SKU : Price", ">=", "899.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    @Test(priority = 13)
    public void skuPrice_moreOrEquals_exactPrice() {

        p.addFilter("SKU : Price", ">=", "900.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    @Test(priority = 14)
    public void skuPrice_moreOrEquals_higherPrice() {

        p.addFilter("SKU : Price", ">=", "999.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    //---- '<'
    @Test(priority = 15)
    public void skuPrice_lessThan_lowerPrice() {

        p.addFilter("SKU : Price", "<", "899.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 16)
    public void skuPrice_lessThan_exactPrice() {

        p.addFilter("SKU : Price", "<", "900.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 17)
    public void skuPrice_lessThan_higherPrice() {

        p.addFilter("SKU : Price", "<", "900.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    //---- '<='

    @Test(priority = 18)
    public void skuPrice_lessOrEquals_lowerPrice() {

        p.addFilter("SKU : Price", "<=", "899.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 19)
    public void skuPrice_lessOrEquals_exactPrice() {

        p.addFilter("SKU : Price", "<=", "900.00");
        p.itemOnList_byPrice("$900.00").shouldNotBe(visible
                .because("Search has found a SKU that has 'Price = $900.00' while it should give no results."));

    }

    @Test(priority = 20)
    public void skuPrice_lessOrEquals_higherPrice() {

        p.addFilter("SKU : Price", "<=", "999.00");
        p.itemOnList_byPrice("$900.00").shouldBe(visible
                .because("Search has failed to find a SKU with requested price value."));

    }

    //--------------- ARCHIVED AT
    //---- '='

    @Test(priority = 21)
    public void archivedAt_Equals_yesterday() throws IOException {

        provideTestData("archived sku");
        p = openPage(adminUrl + "/skus", SkusPage.class);

        p.addFilter("SKU : Archived At", "=", p.yesterday());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At = yesterday' while it should give no results."));

    }

    @Test(priority = 22)
    public void archivedAt_Equals_today() {

        p.addFilter("SKU : Archived At", "=", p.today());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At = today'."));

    }

    //---- '<>'

    @Test(priority = 23)
    public void archivedAt_NotEquals_yesterday() {

        p.addFilter("SKU : Archived At", "<>", p.yesterday());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At <> yesterday'."));

    }

    @Test(priority = 24)
    public void archivedAt_NotEquals_today() {

        p.addFilter("SKU : Archived At", "<>", p.today());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At <> today' while it should give no results."));

    }

    //---- '>'

    @Test(priority = 25)
    public void archivedAt_MoreThan_yesterday() {

        p.addFilter("SKU : Archived At", ">", p.yesterday());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At > yesterday'."));

    }

    @Test(priority = 26)
    public void archivedAt_MoreThan_today() {

        p.addFilter("SKU : Archived At", ">", p.today());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At > today' while it should give no results."));

    }

    @Test(priority = 27)
    public void archivedAt_MoreThan_tomorrow() {

        p.addFilter("SKU : Archived At", ">", p.tomorrow());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At > tomorrow' while it should give no results."));

    }

    //---- '>='

    @Test(priority = 28)
    public void archivedAt_MoreOrEquals_yesterday() {

        p.addFilter("SKU : Archived At", ">=", p.yesterday());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At >= yesterday'."));

    }

    @Test(priority = 29)
    public void archivedAt_MoreOrEquals_today() {

        p.addFilter("SKU : Archived At", ">=", p.today());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At >= today'."));

    }

    @Test(priority = 30)
    public void archivedAt_MoreOrEquals_tomorrow() {

        p.addFilter("SKU : Archived At", ">=", p.tomorrow());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At >= tomorrow' while it should give no results."));

    }

    //---- '<'

    @Test(priority = 31)
    public void archivedAt_LessThan_yesterday() {

        p.addFilter("SKU : Archived At", "<", p.yesterday());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At < yesterday' while it should give no results."));

    }

    @Test(priority = 32)
    public void archivedAt_LessThan_today() {

        p.addFilter("SKU : Archived At", "<", p.today());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At < today' while it should give no results."));

    }

    @Test(priority = 33)
    public void archivedAt_LessThan_tomorrow() {

        p.addFilter("SKU : Archived At", "<", p.tomorrow());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At < tomorrow'."));

    }

    //---- '<='

    @Test(priority = 34)
    public void archivedAt_LessOrEquals_yesterday() {

        p.addFilter("SKU : Archived At", "<=", p.yesterday());
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is 'Archived At <= yesterday' while it should give no results."));

    }

    @Test(priority = 35)
    public void archivedAt_LessOrEquals_today() {

        p.addFilter("SKU : Archived At", "<=", p.today());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At <= today'."));


    }

    @Test(priority = 36)
    public void archivedAt_LessOrEquals_tomorrow() {

        p.addFilter("SKU : Archived At", "<=", p.tomorrow());
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find a SKU that is 'Archived At <= tomorrow'."));

    }

    @Test(priority = 37)
    public void isArchived_yes() {

        p.addFilter("SKU : Is Archived", "Y");
        p.itemOnList(sku).shouldBe(visible
                .because("Search has failed to find an archived SKU."));

    }

    @Test(priority = 38)
    public void isArchived_no() {

        p.addFilter("SKU : Is Archived", "N");
        p.itemOnList(sku).shouldNotBe(visible
                .because("Search has found a SKU that is archived, while it should give no results."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if ($(xpath("//a[text()='×']")).is(visible)) {
            p.cleanSearchField();
        }
    }

}
