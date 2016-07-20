package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.List;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.testng.Assert.assertTrue;

public class StorefrontCategoryPage extends BasePage {

    private SelenideElement itemsOnList_s() {
        return $(By.xpath("//div[@class='_products_item_list_item__list-item']"));
    }

    private SelenideElement searchResults() {
        return $(By.xpath("//section/div[1]/div"));
    }

    public SelenideElement searchBtn() {
        return $(By.xpath("//div[@class='_search_search__search']/div[1]"));
    }

    public SelenideElement searchFld() {
        return $(By.xpath("//div[@class='_header_header__search']/div/input"));
    }

    public String titleVal() {
        SelenideElement title = $(By.xpath("//h1[contains(@class, 'pdp__name')]"));
        return title.text();
    }

    public String priceVal() {
        SelenideElement price = $(By.xpath("//div[contains(@class, 'pdp__price')]/span"));
        String priceVal = price.text();
        return priceVal.substring(1, priceVal.length());
    }

    public String descriptionVal() {
        SelenideElement description = $(By.xpath("//div[contains(@class, 'pdp__description')]"));
        return description.text();
    }

    private SelenideElement noContentMsg() {
        return $(By.xpath("//div[text()='No products found.']"));
    }

    @Step("Wait for search output ot load.")
    public void waitForSearchResultsToLoad() {
        try {
            searchResults().shouldBe(visible);
        } catch (NoSuchElementException nsee) {
            assertTrue( !noContentMsg().is(visible),
                    "Search gave no results." );
        }
    }

    @Step("Wait for data on the list to be loaded.")
    public void waitForDataToLoad_sf() {
        try {
            itemsOnList_s().shouldBe(visible);
        } catch (NoSuchElementException nsee) {
            assertTrue( !noContentMsg().is(visible),
                    "'No products found.' message is displayed." );
        }
    }

    @Step("Find product <{0}> on the list.")
    private boolean lookForProduct(String productName) {
        boolean result = false;

        List<SelenideElement> itemsList = $$(By.xpath("//div[@class='_products_item_list_item__name']"));
        printSEList(itemsList);
        for(SelenideElement item : itemsList) {

            String listItemName = item.text();
            if (listItemName.equals(productName)) {
                result = true;
                break;
            }

        }
        return result;
    }

    @Step("Assert that product <{0}> is displayed in the category")
    public boolean productDisplayed(String productName) {
        waitForDataToLoad_sf();
        return lookForProduct(productName);
    }

    @Step("Assert that product <{0}> is found by search")
    public boolean productIsFound(String productName) {
        waitForSearchResultsToLoad();
        return lookForProduct(productName);
    }

    public void search(String productName) {
        click( searchBtn() );
        setFieldVal( searchFld(), productName );
        searchFld().pressEnter();
    }

    @Step("Open product with name <{0}>.")
    public void openProduct(String productName) {
        click( findProductOnList(productName) );
    }

    @Step("Find on the list a product with name <{0}>")
    private SelenideElement findProductOnList(String productName) {

        waitForDataToLoad_sf();
        List<SelenideElement> productsList = $$(By.xpath("//div[@class='_products_item_list_item__name']"));
        SelenideElement productToClick = null;
        printSEList(productsList);

        for(SelenideElement product : productsList) {

            String listProductName = product.getText();
            if (listProductName.equals(productName)) {
                productToClick = product;
            }

        }

        assertTrue( productToClick != null, "Couldn't find a requested product.");
        return productToClick;

    }

}
