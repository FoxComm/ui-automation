package pages;

import base.BasePage;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;

public class ProductsPage extends BasePage {

    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement itemsOnList() {
        return $(By.xpath("//td[@class='fc-table-td']"));
    }

    public SelenideElement addNewProduct() {
        return $(By.xpath("//span[text()='Product']/.."));
    }

    //-------------------------- NEW PRODUCT
    public SelenideElement titleFld() {
        return $(By.xpath("//input[@name='title']"));
    }

    public SelenideElement descriptionFld() {
        return $(By.xpath("//div[@class='public-DraftEditor-content']"));
    }

    public SelenideElement sku() {
        return $(By.xpath("//tbody/tr[1]/td[2]/div/input"));
    }

    public SelenideElement retailPriceFld() {
        return $(By.xpath("//tbody/tr[1]/td[3]/div/div/input"));
    }

    public SelenideElement salePriceFld() {
        return $(By.xpath("//tbody/tr[1]/td[4]/div/div/input"));
    }

    public SelenideElement stateDd() {
        return $(By.xpath("//div[@class='fc-product-state']/div[2]/div[2]/button"));
    }

    public SelenideElement stateList(String state) {
        return $(By.xpath("//li[text()='" + state + "']"));
    }

    public String stateVal() {
        SelenideElement state = $(By.xpath("//div[@class='fc-product-state']/div[2]/div[2]/div"));
        return state.getText();
    }

    public SelenideElement saveDraftBtn() {
        return $(By.xpath("//span[text()='Save Draft']/.."));
    }
    //--------------------------


    //------------------------------- HELPERS --------------------------------//

    @Step("Wait for data on the list to be loaded.")
    public void waitForDataToLoad() {
        itemsOnList().shouldBe(Condition.visible);
    }

    @Step("Set product state to '{0}'")
    public void setState(String state) {
        click( stateDd() );
        click( stateList(state) );
    }

    @Step("Get '{1}' parameter value of {0}th product on the list")
    public String getProductParamVal(String productIndex, String paramName) {
        String productParamVal = "";
        waitForDataToLoad();
        switch (paramName) {
            case "Product ID":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[2]")).getText();
                break;
//            case "Image":
//                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[3]/div/div")).getText();
//                break;
            case "Name":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[4]")).getText();
                break;
            case "State":
                productParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + productIndex + "]/td[5]/div/div")).getText();
                break;

        }
        return productParamVal;
    }

}
