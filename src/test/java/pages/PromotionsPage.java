package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class PromotionsPage extends BasePage {

    //--------------------------------------- ELEMENTS ---------------------------------------//

    public SelenideElement addNewPromoBtn() {
        return $(xpath("//span[text()='Promotion']/.."));
    }

    public SelenideElement applyTypeDd() {
        return $(xpath("div[@id='apply-type-dd']"));
    }

    public SelenideElement nameFld() {
        return $(xpath("//input[@name='name']"));
    }

    //TODO: Add id in Ashes (Storefrot Name, richtext)
    public SelenideElement storefrontNameFld() {
        return $(xpath(".//*[@id='foxcom']/div/div[1]/main/div/div[3]/form/div[1]/div[1]/div/div[2]/div[2]/div/div[3]/div/div/div"));
    }

    public SelenideElement descriptionFld() {
        return $(xpath("//*[@name='description']"));
    }

    //TODO: Add id in Ashes (Details, richtext)
    public SelenideElement detailsFld() {
        return $(xpath(".//*[@id='foxcom']/div/div[1]/main/div/div[3]/form/div[1]/div[1]/div/div[2]/div[4]/div/div[3]/div/div/div"));
    }

    public SelenideElement qualifierTypeDd() {
        return $(xpath("//div[@id='promo-qualifier-dd']"));
    }

    public SelenideElement offerTypeDd() {
        return $(xpath("//div[@id='promo-offer-dd']"));
    }

    //TODO: Something's fucky - ashes renders different types of <input>'s.
    // Might need to expand logic in tests and add ids in ashes
    private SelenideElement offerGetFld() {
        return $(xpath("//input[@class='fc-append-input__input-field']"));
    }

    public SelenideElement promotionIdVal() {
        return $(xpath("//a[@id='item-id']"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id='state-dd--value']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='remove-start-date-btn']"));
    }

    public SelenideElement promotion(String idOrName) {
        return $(xpath("//tbody/a//td[text()='" + idOrName + "']"));
    }


    //--------------------------------------- HELPERS ----------------------------------------//

    @Step("Create a new promotion with <{0}> apply type")
    public void fillOutNewPromoForm(String applyType, String id) {
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
        SelenideElement paramVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Promotion ID":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'id')]"));
                break;
            case "Name":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'promotionName')]"));
                break;
            case "Storefront Name":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'storefrontName')]"));
                break;
            case "Apply Type":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'applyType')]"));
                break;
            case "Total Uses":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'totalUsed')]"));
                break;
            case "Current Carts":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'currentCarts')]"));
                break;
            case "Date/Time Created":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]//time"));
                break;
            case "State":
                paramVal = $(xpath("//tbody/a[" + promoIndex + "]/td[contains(@class, 'state')]//div[contains(text(), 'ctive')]"));
                break;
        }
        return paramVal;
    }

    @Step("Set promotion \"State\" dd val to <{0}>")
    public void setState(String state) {
        switch (state) {
            case "Inactive":
                click( removeStartDateBtn() );
                break;
            case "Active":
                setDdVal(stateDd(), state);
                break;
        }
    }

    @Step("Open promotion with <ID: {0}>")
    public void openPromo(String promoId) {
        click(promotion(promoId));
    }

}
