package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;

public class StorefrontCategoryPage extends BasePage {

    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement itemsOnList_s() {
        return $(By.xpath("//div[contains(@class, 'list-item')]"));
    }

    public SelenideElement product(String productName) {
        return $(By.xpath("//div[contains(@class, '_list_')]/div/div[text()='" + productName + "']"));
    }

    //.shouldBe(visible.because("Product isn't found on the category page."));

    private SelenideElement searchResult() {
        return $(By.xpath("//section/div[1]/div"));
    }

    private SelenideElement searchBtn() {
        return $(By.xpath("//div[contains(@class, 'header')]/div/div[2]/div/div[1]"));
    }

    public SelenideElement searchFld() {
        return $(By.xpath("//div[contains(@class, 'header')]/div/div[2]/div/input"));
    }

    public SelenideElement titleVal() {
        return $(By.xpath("//h1"));
    }

    public SelenideElement priceVal() {
        return $(By.xpath("//div[contains(@class, '_price_')]/span"));
    }

    public SelenideElement descriptionVal() {
        return $(By.xpath("//div[contains(@class, '_description_')]"));
    }

    private SelenideElement noContentMsg() {
        return $(By.xpath("//div[text()='No products found.']"));
    }


    //------------------------------- HELPERS --------------------------------//

    @Step("Open product with title <{0}> on the category list")
    public void openProduct(String prodTitle) {
        click(product(prodTitle));
    }

    @Step("Wait for search output ot load.")
    public void waitForSearchResultsToLoad() {
        shouldBeVisible(searchResult(), "Search gave no results");
    }

    @Step("Wait for data on the list to be loaded.")
    public void waitForDataToLoad_sf() {
        shouldBeVisible(itemsOnList_s(), "\"No products found.\" message is displayed.");
    }

    public void search(String productName) {
        click(searchBtn());
        setFieldVal(searchFld(), productName);
        searchFld().pressEnter();
    }

}
