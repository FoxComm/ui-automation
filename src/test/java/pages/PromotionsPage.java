package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

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

    public SelenideElement stateVal() {
        return $(By.xpath("//div[text()='State']/../following-sibling::*[1]/div[2]/div"));
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
        setApplyType(applyType);
        setPromoName("Test Promo " + id);
        setStorefrontName("sf name");
        setDescription("test promo");
        setDetails("promo details");
        setQualifierType("Order - No qualifier");
        setOfferType("Percent off order");
        setOfferGet("10");
    }

        @Step("Click \"Create New Promotion\" btn")
        public void clickAddNewPromoBtn() {
            click(addNewPromoBtn());
        }

        @Step("Set \"Apply Type\" dd val to <{0}>")
        public void setApplyType(String type) {
            setDdVal(applyTypeDd(), type);
        }

        @Step("Set \"Name\" fld val to <{0}>")
        public void setPromoName(String name) {
            setFieldVal(nameFld(), name);
        }

        @Step("Set \"Storefront Name\" fld val to <{0}>")
        public void setStorefrontName(String name) {
            setFieldVal(storefrontNameFld(), name);
        }

        @Step("Set \"Description\" rich text fld val to <{0}>")
        public void setDescription(String description) {
            setFieldVal(descriptionFld(), description);
        }

        @Step("Set \"Details\" rich text fld val to <{0}>")
        public void setDetails(String details) {
            setFieldVal(detailsFld(), details);
        }

        @Step("Set \"Qualifyer Type\" dd val to <{0}>")
        public void setQualifierType(String qualifierType) {
            setDdVal(qualifierTypeDd(), qualifierType);
        }

        @Step("Set \"Offer Type\" dd val to <{0}>")
        public void setOfferType(String offerType) {
            setDdVal(offerTypeDd(), offerType);
        }

        @Step("Set \"Offer Get\" fld val to <{0}>")
        public void setOfferGet(String offerGet) {
            setFieldVal(offerGetFld(), offerGet);
        }

    @Step("Create a new promotion with <{0}> apply type")
    public void createNewPromo_autoApply_active(String applyType, String id) {
        clickAddNewPromoBtn();
        setApplyType(applyType);
        setPromoName("Test Promo " + id);
        setStorefrontName("sf name");
        setDescription("test promo");
        setDetails("promo details");
        setQualifierType("Order - No qualifier");
        setOfferType("Percent off order");
        setOfferGet("10");
        setDdVal( stateDd(), "Active" );
        clickSave_wait();
        shouldNotHaveText(promotionIdVal(), "new", "Failed to create a new promotion.");
    }

    @Step("Get <{1}> parameter value of <{0}th> promotion on the list")
    public SelenideElement getPromoParamVal(String promoIndex, String paramName) {
        SelenideElement promoParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Promotion ID":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[2]"));
                break;
            case "Name":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[3]"));
                break;
            case "Storefront Name":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[4]"));
                break;
            case "Apply Type":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[5]"));
                break;
            case "Total Uses":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[6]"));
                break;
            case "Current Carts":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[7]"));
                break;
            case "Date/Time Created":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[8]/time"));
                break;
            case "State":
                promoParamVal = $(By.xpath("//tbody[@class='fc-table-body']/a[" + promoIndex + "]/td[9]/div/div"));
                break;
        }
        return promoParamVal;
    }

    @Step("Set promotion \"State\" dd val to <{0}>")
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

    @Step("Open promotion with <ID: {0}>")
    public void openPromo(String promoId) {
        click(promotion(promoId));
    }

}
