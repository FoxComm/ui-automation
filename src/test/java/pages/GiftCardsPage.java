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
        return $(xpath("//h1/span/span/span"));
    }

    public SelenideElement addNewGCBtn() {
        return $(xpath("//span[text()='Gift Card']/.."));
    }

    public SelenideElement typeDd() {
        return $(xpath("//label[text()='Gift Card Type']/following-sibling::*/div[2]/button"));
    }

    public SelenideElement valueFld() {
        return $(xpath("//input[contains(@class, 'fc-prepend-input')]"));
    }

    public SelenideElement stateDd() {
        return $(xpath("//div[text()='Current State']/following-sibling::*/div/div[2]/button"));
    }

    private SelenideElement stateOpt(String state) {
        return $(xpath("//li[text()='Hold']"));
    }

    public SelenideElement stateVal() {
        return $(xpath("//div[text()='Current State']/following-sibling::*//input"));
    }

    public SelenideElement qtyIncrBtn() {
        return $(xpath("//i[@class='icon-chevron-up']/.."));
    }

    public SelenideElement issueGCBtn() {
        return $(xpath("//span[text()='Issue Gift Card']/.."));
    }

    public SelenideElement availableBalance() {
        return $(xpath("//div[text()='Available Balance']/following-sibling::*/span"));
    }

    public SelenideElement presetValue(String val) {
        return $(xpath("//div[text()='" + val + "']"));
    }

    public SelenideElement cancelReasonDd() {
        return $(xpath("//label[text()='Cancel Reason']/../following-sibling::*/div/div[2]/button"));
    }

    public SelenideElement yesBtn() {
        return $(xpath("//span[contains(text(), 'Yes')]/.."));
    }

    public SelenideElement cancelBtn() {
        return $(xpath("//a[text()='Cancel']"));
    }

    public SelenideElement gcToCustomerChbx() {
        return $(xpath("//input[@name='sendToCustomer']"));
    }

    public SelenideElement chooseCustomerFld() {
        return $(xpath("//input[@name='customerQuery']"));
    }

    public SelenideElement messageFld() {
        return $(xpath("//textarea[@name='customerMessage']"));
    }

    private SelenideElement selectCustomerChbx(String name) {
        return $(xpath("//span[text()='" + name + "']/../preceding-sibling::*"));
    }

    public SelenideElement addCustomerBtn() {
        return $(xpath("//span[text()='Add Customers']/.."));
    }

    public SelenideElement chosenCustomer(String customerName) {
        return $(xpath("//div[text()='" + customerName + "']"));
    }

    public SelenideElement gcAvailableBalance() {
        return $(xpath("//div[text()='Available Balance']/following-sibling::*/span"));
    }


    //------------------------------ HELPERS ---------------------------------//

    public String getGCNumber(String url, String adminUrl) {
        return url.substring((adminUrl.length() + 11), (adminUrl.length() + 27));
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

    @Step("Set amount of GCs to issue to <QTY:{0}+1(default amount)>")
    public void increaseQtyBy(int increaseBy) {
        for(int i = 0; i < increaseBy; i++){
            click(qtyIncrBtn());
        }
    }

    @Step("Set GC's \"State\" dd val to <{0}>")
    public void setState(String state) {
        click(stateDd());
        click(stateOpt(state));
    }

    @Step("Select cancellation reason <{0}>")
    public void setCancelReason(String reason) {
        setDdVal(cancelReasonDd(), reason);
    }

    @Step("Issue GC to customer <{0}>")
    public void issueGCToCustomer(String customerName, String message) {
        clickCheckbox();
        setCustomerName(customerName);
        selectCustomer(customerName);
        clickAddCustomerBtn();
        shouldBeVisible(chosenCustomer(customerName), "Customer is not selected");
        addMsgToCustomer(message);
    }

        @Step("Check \"Send gift card(s) to customer(s)\" checkbox")
        private void clickCheckbox() {
            jsClick(gcToCustomerChbx());
        }

        @Step("Set \"Choose Customers\" fld val to <{0}>")
        public void setCustomerName(String name) {
            setFieldVal(chooseCustomerFld(), name);
        }

        @Step("Select on the list a customer with name <{0}>")
        public void selectCustomer(String customerQuery) {
            jsClick(selectCustomerChbx(customerQuery));
            sleep(1500);
        }

        @Step("Click \"Add Customer\" btn")
        private void clickAddCustomerBtn() {
            click(addCustomerBtn());
        }

        @Step("Specify message for a customer in according text area")
        private void addMsgToCustomer(String message) {
            setFieldVal(messageFld(), message);
        }

    @Step("Get <{1}> parameter value of <{0}th> gift card on the list")
    public SelenideElement getGCParamVal(String gcIndex, String paramName) {
        SelenideElement gcParamVal = null;
        waitForDataToLoad();
        switch (paramName) {
            case "Gift Card Number":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[2]"));
                break;
            case "Type":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[3]/div/div"));
                break;
            case "Original Balance":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[4]/td/span"));
                break;
            case "Current Balance":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[5]"));
                break;
            case "Available Balance":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[6]"));
                break;
            case "State":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[7]/span"));
                break;
            case "Date/Time Created":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[8]/time"));
                break;
        }
        return gcParamVal;
    }

}
