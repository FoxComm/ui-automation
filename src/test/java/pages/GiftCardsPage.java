package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class GiftCardsPage extends BasePage {

    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement counter() {
        return $(xpath("//span[@id='total-counter-value']"));
    }

    public SelenideElement addNewGCBtn() {
        return $(xpath("//span[text()='Gift Card']/.."));
    }

    public SelenideElement typeDd() {
        return $(xpath("//div[@id='gift-card-type-dd']"));
    }

    public SelenideElement valueFld() {
        return $(xpath("//input[@name='balance']"));
    }

    public SelenideElement presetValue(String val) {
        return $(xpath("//div[text()='" + val + "']"));
    }

    public SelenideElement qtyIncrBtn() {
        return $(xpath("//button[contains(@class, 'increment')]"));
    }

    public SelenideElement issueGCBtn() {
        return $(xpath("//span[text()='Issue Gift Card']/.."));
    }

    public SelenideElement availableBalance() {
        return $(xpath("//*[@id='gift-card-available-balance']"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[@id='gift-card-state-dd']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[@id='gift-card-state-dd']//div[@class='fc-dropdown__value']"));
    }

    //TODO: add id in ashes
    public SelenideElement cancelReasonDd() {
        return $(xpath("//label[text()='Cancel Reason']/../following-sibling::*/div/div[2]/button"));
    }

    public SelenideElement yesBtn() {
        return $(xpath("//button[@id='modal-confirm-btn']"));
    }

    public SelenideElement cancelBtn() {
        return $(xpath("//button[@id='modal-cancel-btn']"));
    }

//    public SelenideElement gcToCustomerChbx() {
//        return $(xpath("//input[@name='sendToCustomer']"));
//    }
//
//    public SelenideElement chooseCustomerFld() {
//        return $(xpath("//input[@name='customerQuery']"));
//    }
//
//    public SelenideElement messageFld() {
//        return $(xpath("//textarea[@name='customerMessage']"));
//    }
//
//    private SelenideElement selectCustomerChbx(String name) {
//        return $(xpath("//span[text()='" + name + "']/../preceding-sibling::*"));
//    }
//
//    public SelenideElement addCustomerBtn() {
//        return $(xpath("//span[text()='Add Customers']/.."));
//    }
//
//    public SelenideElement chosenCustomer(String customerName) {
//        return $(xpath("//div[text()='" + customerName + "']"));
//    }


    //------------------------------ HELPERS ---------------------------------//

    public String getGCNumber(String url, String adminUrl) {
        return url.substring((adminUrl.length() + 12), (adminUrl.length() + 28));
    }

    @Step("Click \"Add New GC\" btn")
    public void clickAddMewGCBtn() {
        click(addNewGCBtn());
    }

    @Step("Issue new GC with value <{0}>")
    public void issueGC(String gcVal) {
        setType("Appeasement");
        setValue(gcVal);
        clickIssueGCBtn();
        shouldBeVisible(availableBalance(), "Waiting for \"Available Balance\" to become visible has failed");
    }

        @Step("Set \"Type\" dd val to <{0}>")
        public void setType(String type) {
            setDdVal(typeDd(), type);
        }

        @Step("Set \"Value\" fld val to <{0}>")
        public void setValue(String value) {
            setFieldVal(valueFld(), value);
        }

        @Step("Click \"Issue Gift Card\" btn")
        public void clickIssueGCBtn() {
            click(issueGCBtn());
        }

        @Step("Select preset value, <amount:{0}>")
        public void setPresetValue(String value) {
            click(presetValue(value));
        }

    @Step("Increase amount of GCs to be issued by <{0}>")
    public void increaseQtyBy(int increaseBy) {
        for(int i = 0; i < increaseBy; i++){
            click(qtyIncrBtn());
        }
    }

    @Step("Set \"State\" dd val to <{0}>")
    public void setState(String state) {
        setDdVal(stateDd(), state);
    }

    @Step("Select cancellation reason <{0}>")
    public void setCancelReason(String reason) {
        setDdVal(cancelReasonDd(), reason);
    }

//    @Step("Issue GC to customer <{0}>")
//    public void issueGCToCustomer(String customerName, String message) {
//        clickCheckbox();
//        setCustomerName(customerName);
//        selectCustomer(customerName);
//        clickAddCustomerBtn();
//        shouldBeVisible(chosenCustomer(customerName), "Customer is not selected");
//        addMsgToCustomer(message);
//    }
//
//        @Step("Check \"Send gift card(s) to customer(s)\" checkbox")
//        private void clickCheckbox() {
//            jsClick(gcToCustomerChbx());
//        }
//
//        @Step("Set \"Choose Customers\" fld val to <{0}>")
//        public void setCustomerName(String name) {
//            setFieldVal(chooseCustomerFld(), name);
//        }
//
//        @Step("Select on the list a customer with name <{0}>")
//        public void selectCustomer(String customerQuery) {
//            jsClick(selectCustomerChbx(customerQuery));
//            sleep(1500);
//        }
//
//        @Step("Click \"Add Customer\" btn")
//        private void clickAddCustomerBtn() {
//            click(addCustomerBtn());
//        }
//
//        @Step("Specify message for a customer in according text area")
//        private void addMsgToCustomer(String message) {
//            setFieldVal(messageFld(), message);
//        }

    @Step("Get <{1}> parameter value of <{0}th> gift card on the list")
    public SelenideElement getGCParamVal(String gcIndex, String paramName) {
        SelenideElement gcParamVal = null;
        waitForDataToLoad();

        switch (paramName) {
            case "Gift Card Number":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]/td[contains(@class, 'code')]"));
                break;
            case "Type":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]//div[contains(@class, 'gift-card-type')]"));
                break;
            case "Original Balance":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]/td[contains(@class, 'original-balance')]"));
                break;
            case "Current Balance":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]/td[contains(@class, 'current-balance')]"));
                break;
            case "Available Balance":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]/td[contains(@class, 'available-balance')]"));
                break;
            case "State":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]/td[contains(@class, 'state')]//div[contains(text(), 'ctive')]"));
                break;
            case "Date/Time Created":
                gcParamVal = $(xpath("//tbody/a[" + gcIndex + "]//time"));
                break;
        }

        return gcParamVal;
    }

}