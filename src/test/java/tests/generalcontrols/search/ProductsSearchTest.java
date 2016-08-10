package tests.generalcontrols.search;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pages.LoginPage;
import pages.ProductsPage;
import testdata.DataProvider;

import java.io.IOException;
import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.openqa.selenium.By.xpath;

public class ProductsSearchTest extends DataProvider {

    private ProductsPage p;

    @BeforeClass(alwaysRun = true)
    public void setUp() throws IOException {

        open(adminUrl);
        if ((Objects.equals(getUrl(), adminUrl + "/login"))) {
            LoginPage loginPage = open(adminUrl + "/login", LoginPage.class);
            loginPage.login("admin@admin.com", "password");
        }
        provideTestData("product for search tests");
        p = open(adminUrl + "/products", ProductsPage.class);

    }

    @Test(priority = 1)
    public void productId() {

        p.addFilter("Product : ID", productId);
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product with requested ID."));

    }

    @Test(priority = 1)
    public void productName() {

        p.addFilter("Product : Name", productName);
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product with requested ID."));

    }

    //--------------- ACTIVE FROM
    //---- '='

    @Test(priority = 2)
    public void activeFrom_Equals_yesterday() {

        p.addFilter("Product : Active From", "=", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From = yesterday' while it should give no results."));

    }

    @Test(priority = 3)
    public void activeFrom_Equals_today() {

        p.addFilter("Product : Active From", "=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From = today'."));

    }

    @Test(priority = 4)
    public void activeFrom_Equals_tomorrow() {

        p.addFilter("Product : Active From", "=", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From = tomorrow' while it should give no results."));

    }

    //---- '<>'

    @Test(priority = 5)
    public void activeFrom_NotEquals_yesterday() {

        p.addFilter("Product : Active From", "<>", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From <> yesterday'."));

    }

    @Test(priority = 6)
    public void activeFrom_NotEquals_today() {

        p.addFilter("Product : Active From", "<>", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From <> today' while it should give no results."));

    }

    //---- '>'

    @Test(priority = 7)
    public void activeFrom_MoreThan_yesterday() {

        p.addFilter("Product : Active From", ">", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From > yesterday'."));

    }

    @Test(priority = 8)
    public void activeFrom_MoreThan_today() {

        p.addFilter("Product : Active From", ">", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From > today' while it should give no results."));

    }

    @Test(priority = 9)
    public void activeFrom_MoreThan_tomorrow() {

        p.addFilter("Product : Active From", ">", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From > tomorrow' while it should give no results."));

    }

    //---- '>='

    @Test(priority = 10)
    public void activeFrom_MoreOrEquals_yesterday() {

        p.addFilter("Product : Active From", ">=", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From >= yesterday'."));

    }

    @Test(priority = 11)
    public void activeFrom_MoreOrEquals_today() {

        p.addFilter("Product : Active From", ">=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From >= today'."));

    }

    @Test(priority = 12)
    public void activeFrom_MoreOrEquals_tomorrow() {

        p.addFilter("Product : Active From", ">=", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From >= tomorrow' while it should give no results."));

    }

    //---- '<'

    @Test(priority = 13)
    public void activeFrom_LessThan_yesterday() {

        p.addFilter("Product : Active From", "<", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From < yesterday' while it should give no results."));

    }

    @Test(priority = 14)
    public void activeFrom_LessThan_today() {

        p.addFilter("Product : Active From", "<", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From < today' while it should give no results."));

    }

    @Test(priority = 15)
    public void activeFrom_LessThan_tomorrow() {

        p.addFilter("Product : Active From", "<", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From < tomorrow'."));

    }

    //---- '<='

    @Test(priority = 16)
    public void activeFrom_LessOrEquals_yesterday() {

        p.addFilter("Product : Active From", "<=", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active From <= yesterday' while it should give no results."));

    }

    @Test(priority = 17)
    public void activeFrom_LessOrEquals_today() {

        p.addFilter("Product : Active From", "<=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From <= today'."));


    }

    @Test(priority = 18)
    public void activeFrom_LessOrEquals_tomorrow() {

        p.addFilter("Product : Active From", "<=", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active From <= tomorrow'."));

    }

    //--------------- ACTIVE TO
    //---- '='

    @Test(priority = 19)
    public void activeTo_Equals_today() {

        p.addFilter("Product : Active To", "=", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active to = today' while it should give no results."));

    }

    @Test(priority = 20)
    public void activeTo_Equals_tomorrow() {

        p.addFilter("Product : Active To", "=", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To = tomorrow'."));

    }

    //---- '<>'

    @Test(priority = 21)
    public void activeTo_NotEquals_yesterday() {

        p.addFilter("Product : Active To", "<>", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To <> yesterday'."));

    }

    @Test(priority = 22)
    public void activeTo_NotEquals_today() {

        p.addFilter("Product : Active To", "<>", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To <> today'."));

    }

    @Test(priority = 23)
    public void activeTo_NotEquals_tomorrow() {

        p.addFilter("Product : Active To", "<>", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active To <> tomorrow' while it should give no results."));

    }

    //---- '>'

    @Test(priority = 24)
    public void activeTo_MoreThan_yesterday() {

        p.addFilter("Product : Active To", ">", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To > yesterday'."));

    }

    @Test(priority = 25)
    public void activeTo_MoreThan_today() {

        p.addFilter("Product : Active To", ">", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To > today'."));

    }

    @Test(priority = 26)
    public void activeTo_MoreThan_tomorrow() {

        p.addFilter("Product : Active To", ">", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active To > tomorrow' while it should give no results."));

    }

    //---- '>='

    @Test(priority = 27)
    public void activeTo_MoreOrEquals_today() {

        p.addFilter("Product : Active To", ">=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To >= today'."));

    }

    @Test(priority = 28)
    public void activeTo_MoreOrEquals_tomorrow() {

        p.addFilter("Product : Active To", ">=", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To >= today'."));

    }

    @Test(priority = 29)
    public void activeTo_MoreOrEquals_afterTomorrow() {

        p.addFilter("Product : Active To", ">=", p.afterTomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active To >= afterTomorrow' while it should give no results."));

    }

    //---- '<'

    @Test(priority = 30)
    public void activeTo_LessThan_tomorrow() {

        p.addFilter("Product : Active To", "<", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active To < tomorrow' while it should give no results."));

    }

    @Test(priority = 31)
    public void activeTo_LessThan_afterTomorrow() {

        p.addFilter("Product : Active To", "<", p.afterTomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To < afterTomorrow'."));

    }

    //---- '<='

    @Test(priority = 32)
    public void activeTo_LessOrEquals_today() {

        p.addFilter("Product : Active To", "<=", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Active To <= today' while it should give no results."));

    }

    @Test(priority = 33)
    public void activeTo_LessOrEquals_tomorrow() {

        p.addFilter("Product : Active To", "<=", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To <= tomorrow'."));

    }

    @Test(priority = 34)
    public void activeTo_LessOrEquals_afterTomorrow() {

        p.addFilter("Product : Active To", "<=", p.afterTomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Active To <= afterTomorrow'."));

    }

    //--------------- ARCHIVED AT
    //---- '='

    @Test(priority = 35)
    public void archivedAt_Equals_yesterday() throws IOException {

        provideTestData("archived product");
        p = open(adminUrl + "/products", ProductsPage.class);

        p.addFilter("Product : Archived At", "=", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At = yesterday' while it should give no results."));

    }

    @Test(priority = 36)
    public void archivedAt_Equals_today() {

        p.addFilter("Product : Archived At", "=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At = today'."));

    }

    //---- '<>'

    @Test(priority = 37)
    public void archivedAt_NotEquals_yesterday() {

        p.addFilter("Product : Archived At", "<>", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At <> yesterday'."));

    }

    @Test(priority = 38)
    public void archivedAt_NotEquals_today() {

        p.addFilter("Product : Archived At", "<>", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At <> today' while it should give no results."));

    }

    //---- '>'

    @Test(priority = 39)
    public void archivedAt_MoreThan_yesterday() {

        p.addFilter("Product : Archived At", ">", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At > yesterday'."));

    }

    @Test(priority = 40)
    public void archivedAt_MoreThan_today() {

        p.addFilter("Product : Archived At", ">", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At > today' while it should give no results."));

    }

    @Test(priority = 41)
    public void archivedAt_MoreThan_tomorrow() {

        p.addFilter("Product : Archived At", ">", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At > tomorrow' while it should give no results."));

    }

    //---- '>='

    @Test(priority = 42)
    public void archivedAt_MoreOrEquals_yesterday() {

        p.addFilter("Product : Archived At", ">=", p.yesterday());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At >= yesterday'."));

    }

    @Test(priority = 43)
    public void archivedAt_MoreOrEquals_today() {

        p.addFilter("Product : Archived At", ">=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At >= today'."));

    }

    @Test(priority = 44)
    public void archivedAt_MoreOrEquals_tomorrow() {

        p.addFilter("Product : Archived At", ">=", p.tomorrow());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At >= tomorrow' while it should give no results."));

    }

    //---- '<'

    @Test(priority = 45)
    public void archivedAt_LessThan_yesterday() {

        p.addFilter("Product : Archived At", "<", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At < yesterday' while it should give no results."));

    }

    @Test(priority = 46)
    public void archivedAt_LessThan_today() {

        p.addFilter("Product : Archived At", "<", p.today());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At < today' while it should give no results."));

    }

    @Test(priority = 47)
    public void archivedAt_LessThan_tomorrow() {

        p.addFilter("Product : Archived At", "<", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At < tomorrow'."));

    }

    //---- '<='

    @Test(priority = 48)
    public void archivedAt_LessOrEquals_yesterday() {

        p.addFilter("Product : Archived At", "<=", p.yesterday());
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is 'Archived At <= yesterday' while it should give no results."));

    }

    @Test(priority = 49)
    public void archivedAt_LessOrEquals_today() {

        p.addFilter("Product : Archived At", "<=", p.today());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At <= today'."));


    }

    @Test(priority = 50)
    public void archivedAt_LessOrEquals_tomorrow() {

        p.addFilter("Product : Archived At", "<=", p.tomorrow());
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find a product that is 'Archived At <= tomorrow'."));

    }

    //--------

    @Test(priority = 51)
    public void isArchived_yes() {

        p.addFilter("Product : Is Archived", "Y");
        p.itemOnList(productId).shouldBe(visible
                .because("Search has failed to find an archived product."));

    }

    @Test(priority = 52)
    public void isArchived_no() {

        p.addFilter("Product : Is Archived", "N");
        p.itemOnList(productId).shouldNotBe(visible
                .because("Search has found a product that is archived while it should give no results."));

    }

    @Test(priority = 53)
    public void archivedAtLogicCorrectnessTest() {

        p = open(adminUrl + "/products", ProductsPage.class);

        p.addFilter("Product : Active From", "=", p.today());
        p.addFilter("Product : ID", productId);
        p.itemOnList(productId).shouldNotBe(visible
                .because("An archived product is displayed on the list while it shouldn't."));

    }

    @AfterMethod(alwaysRun = true)
    public void cleanUp() {
        if ($(xpath("//a[text()='×']")).is(visible)) {
            p.cleanSearchField();
        }
    }

}