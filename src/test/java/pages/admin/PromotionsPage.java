package pages.admin;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class PromotionsPage extends BasePage {

    //--------------------------------------- ELEMENTS ---------------------------------------//

    private SelenideElement addNewPromoBtn() {
        return $(xpath("//span[text()='Promotion']/.."));
    }

    public SelenideElement applyTypeRbtn(String type) {
        return $(xpath("//input[@name='" + type + "']"));
    }

    private SelenideElement qualifierTypeDd() {
        return $(xpath("//div[@id='fct-promo-qualifier-dd']"));
    }

    private SelenideElement spendFld_qualifier() {
        return $(xpath("//*[@id='fct-promo-qualifier-block']//div[contains(@class, 'promotions_attrs')]/input"));
    }

    private SelenideElement qtyFld_qualifier() {
        return $(xpath("//*[@id='fct-promo-qualifier-block']//input[contains(@class, 'adjust-quantity-input')]"));
    }

    private SelenideElement modifierDd_qualifier() {
        return $(xpath("//*[@id='fct-promo-qualifier-block-0']//div[contains(@class, 'modifier-dd')]"));
    }

    private SelenideElement productSearchDd_qualifier() {
        return $(xpath("//*[@id='fct-promo-qualifier-block']//div[contains(@class, 'select-product-search-dd')]"));
    }

    private SelenideElement offerTypeDd() {
        return $(xpath("//div[@id='fct-promo-offer-dd']"));
    }

    private SelenideElement offerInput_percent() {
        return $(xpath("//*[@id='fct-promo-offer-block']//input[@class='fc-append-input__input-field']"));
    }

    private SelenideElement offerInput_currency() {
        return $(xpath("//*[@id='fct-promo-offer-block']//input[@name='currencyInput']"));
    }

    private SelenideElement productDd_offer() {
        return $(xpath("//*[@id='fct-promo-offer-block']//div[contains(@class, 'select-product-dd')]"));
    }

    private SelenideElement modifierDd_offer() {
        return $(xpath("//*[@id='fct-promo-offer-block']//div[contains(@class, 'modifier-dd')]"));
    }

    private SelenideElement productSearchDd_offer() {
        return $(xpath("//*[@id='fct-promo-offer-block']//div[contains(@class, 'select-product-search-dd')]"));
    }

    public SelenideElement promoIdBreadcrumb() {
        return $(xpath("//a[@id='fct-breadcrumbs-id']"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[@id='state-dd']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id='state-dd-value']"));
    }

    private SelenideElement removeStartDateBtn() {
        return $(xpath("//a[@id='fct-remove-start-date-btn']"));
    }

    private SelenideElement promotion(String idOrName) {
        return $(xpath("//tbody/a//td[text()='" + idOrName + "']"));
    }


    //--------------------------------------- HELPERS ----------------------------------------//

    @Step("Click \"Save\" and wait until it's re-enabled")
    public void clickSave_wait() {
        click(saveBtn());
        sleep(1000);
        shouldNotBeVisible($(xpath("//button[@id='fct-primary-save-btn' and contains(@class, 'loading')]")),
                "\"Save\" btn doesn't get re-enabled");
        sleep(2000);
    }

    @Step("Create a new promotion with <{0}> apply type")
    public void fillOutNewPromoForm(String applyType, String id) {
        setApplyType(applyType);
        setQualifierType("Order - No qualifier");
        setOffer("Percent off order", "10");
    }

        @Step("Click \"Create New Promotion\" btn")
        private void clickAddNewPromoBtn() {
            click(addNewPromoBtn());
        }

        @Step("Set \"Apply Type\" dd val to <{0}>")
        public void setApplyType(String type) {
            jsClick(applyTypeRbtn(type.toLowerCase()));
        }

        @Step("Set \"Qualifyer Type\" dd val to <{0}>")
        private void setQualifierType(String qualifierType) {
            setDdVal_li(qualifierTypeDd(), qualifierType);
        }

        @Step("Set \"Offer Type\" dd val to <{0}>")
        private void setOfferType(String offerType) {
            setDdVal_li(offerTypeDd(), offerType);
        }

        @Step("Set \"Offer Get\" fld val to <${0}>")
        private void setOfferGet_currency(String offer) {
            setFieldVal(offerInput_currency(), offer);
        }

        @Step("Set \"Offer Get\" fld val to <{0}%>")
        private void setOfferGet_percent(String offer) {
            setFieldVal(offerInput_percent(), offer);
        }

        @Step("Set offer type:<{0}>, value:<{1}>")
        private void setOffer(String type, String offer) {
            setOfferType(type);
            String typeToSet = definePromoOfferType(type);
            switch (typeToSet) {
                case "percent":
                    setOfferGet_percent(offer);
                    break;
                case "currency":
                    setOfferGet_currency(offer);
                    break;
                case "free shipping":
                    break;
                case "unknown offer type":
                    throw new RuntimeException("Unknown offer type is given: <" + type + ">");
            }
        }

    @Step("Create a new promotion with <{0}> apply type")
    public void createNewPromo_autoApply_active(String applyType, String id) {
        clickAddNewPromoBtn();
        setApplyType(applyType);
        setQualifierType("Order - No qualifier");
        setOffer("Percent off order", "10");
        setDdVal( stateDd(), "Active" );
        clickSave_wait();
        shouldNotHaveText(promoIdBreadcrumb(), "new", "Failed to create a new promotion.");
    }

    @Step("Set qualifier's \"Spend\" input fld to <{0}>")
    public void setSpendFld_qualifier(String spend) {
        setFieldVal(spendFld_qualifier(), spend);
    }

    @Step("Set qualifier's \"Items Qty\" input fld to <{0}>")
    public void setItemsQtyFld_qualifier(String qty) {
        setFieldVal(qtyFld_qualifier(), qty);
    }

    @Step("Select <{0}> value in modifier dd at \"Items for qualify\" block")
    public void setModifierDd_qualifier(String modifer) {
        setDdVal(modifierDd_qualifier(), modifer);
    }

    @Step("Select <{0}> value in \"Select Product Search\" dd at \"Items for qualify\" block")
    public void setProductSearchDd_qualifier(String productSearchDd) {
        setDdVal(productSearchDd_qualifier(), productSearchDd);
    }

    @Step("Select <{0}> value in \"Select Product\" dd at \"Offer-Get\" block")
    public void setProductDd_offer(String qty) {
        setDdVal((productDd_offer()), qty);
    }

    @Step("Select <{0}> value in modifier dd at \"Discount the Items\" block")
    public void setModifierDd_offer(String modifer) {
        setDdVal(modifierDd_offer(), modifer);
    }

    @Step("Select <{0}> value in \"Select Product Search\" dd at \"Discount the Items\" block")
    public void setProductSearchDd_offer(String productSearchDd) {
        setDdVal(productSearchDd_offer(), productSearchDd);
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
