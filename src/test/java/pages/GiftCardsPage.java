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

    public String stateVal() {
        return $(xpath("//div[text()='Current State']/following-sibling::*/div/div[2]/div")).text();
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

    public String availableBalanceVal() {
        String availableBalVal = availableBalance().text();
        return availableBalVal.substring( 1, availableBalVal.length() );
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



//li[@class='fc-choose-customers__entry']

    //------------------------------ HELPERS ---------------------------------//

    @Step("Get '{1}' parameter value of {0}th gift card on the list")
    public String getGCParamVal(String gcIndex, String paramName) {

        String gcParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Gift Card Number":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[2]")).getText();
                break;
            case "Type":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[3]/div/div")).getText();
                break;
            case "Original Balance":
                String originalBalance = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[4]/td/span")).getText();
                gcParamVal = originalBalance.substring(1, originalBalance.length());
                break;
            case "Current Balance":
                String currentBalance = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[5]")).getText();
                gcParamVal = currentBalance.substring(1, currentBalance.length());
                break;
            case "Available Balance":
                String availableBalance = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[6]")).getText();
                gcParamVal = availableBalance.substring(1, availableBalance.length());
                break;
            case "State":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[7]/span")).getText();
                break;
            case "Date/Time Created":
                gcParamVal = $(xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[8]/time")).getText();
                break;
        }

        return gcParamVal;

    }

    @Step("Select on the list a customer with name <{0}>.")
    public void selectCustomer(String customerQuery) {

        jsClick( selectCustomerChbx(customerQuery) );
        sleep(1500);

    }

}
