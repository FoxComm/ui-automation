package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class PromotionsPage extends BasePage {

    //--------------------------------------- ELEMENTS ---------------------------------------//

    public SelenideElement addNewPromoBtn() {
        return $(By.xpath("//span[text()='Promotion']/.."));
    }

    public SelenideElement applyTypeDd() {
        return $(By.xpath("//label[text()='Apply Type']/following-sibling::*[1]/div/div[2]/button"));
    }

    public SelenideElement nameFld() {
        return $(By.xpath("//input[@name='name']"));
    }

    public SelenideElement storefrontNameFld() {
        return $(By.xpath(".//*[@id='foxcom']/div/div[1]/main/div/div[3]/form/div[1]/div[1]/div/div[2]/div[2]/div/div[3]/div/div/div"));
    }

    public SelenideElement descriptionFld() {
        return $(By.xpath(".//*[@id='foxcom']/div/div[1]/main/div/div[3]/form/div[1]/div[1]/div/div[2]/div[3]/div/div[3]/div/div/div"));
    }

    public SelenideElement detailsFld() {
        return $(By.xpath(".//*[@id='foxcom']/div/div[1]/main/div/div[3]/form/div[1]/div[1]/div/div[2]/div[4]/div/div[3]/div/div/div"));
    }

    public SelenideElement qualifierTypeDd() {
        return $(By.xpath("//div[text()='Qualifier Type']/following-sibling::*/div/div/div/div[2]/button/i"));
    }

    public SelenideElement offerTypeDd() {
        return $(By.xpath("//div[text()='Offer Type']/following-sibling::*/div/div/div/div[2]/button"));
    }

    public SelenideElement offerGetFld() {
        return $(By.xpath("//input[@class='fc-append-input__input-field']"));
    }

    public SelenideElement promotionIdVal() {
        return $(By.xpath("//div[@class='fc-breadcrumbs']/ul/li[5]/a"));
    }

    public SelenideElement stateDd() {
        return $(By.xpath("//div[text()='State']/../following-sibling::*/div[2]/button"));
    }

    private SelenideElement stateListVal(String state) {
        return $(By.xpath("//li[text()='" + state + "']"));
    }

    public String stateVal() {
        SelenideElement stateVal = $(By.xpath("//div[text()='State']/../following-sibling::*[1]/div[2]/div"));
        return stateVal.text();
    }

    private SelenideElement removeStartDateBtn() {
        return $(By.xpath("//a[@class='fc-date-time-picker__close']"));
    }

    public SelenideElement promotion(String idOrName) {
        return $(By.xpath("//tbody[@class='fc-table-body']/a/td[text()='" + idOrName + "']"));
    }


    //--------------------------------------- HELPERS ----------------------------------------//

    @Step("Create a new promotion with <{0}> apply type")
    public void createNewPromo(String applyType, String id) {

        click( addNewPromoBtn() );
        setDdVal( applyTypeDd(), applyType );
        setFieldVal( nameFld(), "Test Promo " + id );
        setFieldVal( storefrontNameFld(), "sf name" );
        setFieldVal( descriptionFld(), "test promo" );
        setFieldVal( detailsFld(), "promo details" );
        setDdVal( qualifierTypeDd(), "Order - No qualifier" );
        setDdVal( offerTypeDd(), "Percent off order" );
        setFieldVal( offerGetFld(), "10" );
        clickSave();
        promotionIdVal().shouldNotHave(text("new")
                .because("Failed to create a new promotion."));

    }

    @Step("Create a new promotion with <{0}> apply type")
    public void createNewPromo_autoApply_active(String applyType, String id) {

        click( addNewPromoBtn() );
        setDdVal( applyTypeDd(), applyType );
        setFieldVal( nameFld(), "Test Promo " + id );
        setFieldVal( storefrontNameFld(), "sf name" );
        setFieldVal( descriptionFld(), "test promo" );
        setFieldVal( detailsFld(), "promo details" );
        setDdVal( qualifierTypeDd(), "Order - No qualifier" );
        setDdVal( offerTypeDd(), "Percent off order" );
        setFieldVal( offerGetFld(), "10" );
        setDdVal( stateDd(), "Active" );
        clickSave();
        promotionIdVal().shouldNotHave(text("new")
                .because("Failed to create a new promotion."));

    }

    @Step("Get '{1}' parameter value of {0}th promotion on the list")
    public String getPromoParamVal(String promoIndex, String paramName) {
        String promoParamVal = "";
        waitForDataToLoad();
        switch (paramName) {
            case "Promotion ID":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[2]")).getText();
                break;
            case "Name":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[3]")).getText();
                break;
            case "Storefront Name":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[4]")).getText();
                break;
            case "Apply Type":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[5]")).getText();
                break;
            case "Total Uses":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[6]")).getText();
                break;
            case "Current Carts":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[7]")).getText();
                break;
            case "Date/Time Created":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[8]/time")).getText();
                break;
            case "State":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[9]/div/div")).getText();
                break;

        }
        return promoParamVal;
    }

    @Step("Set promotion's state to <{0}>")
    public void setState(String state) {

        switch (state) {
            case "Inactive":
                click( removeStartDateBtn() );
                break;
            case "Active":
                click( stateDd() );
                click( stateListVal(state) );
                break;
        }

    }

}
