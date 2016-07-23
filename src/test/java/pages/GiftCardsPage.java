package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;

public class GiftCardsPage extends BasePage {

    //------------------------------ ELEMENTS --------------------------------//

    public SelenideElement addNewGCBtn() {
        return $(By.xpath("//span[text()='Gift Card']/.."));
    }

    public SelenideElement typeDd() {
        return $(By.xpath("//label[text()='Gift Card Type']/following-sibling::*/div[2]/button"));
    }

    public SelenideElement valueFld() {
        return $(By.xpath("//input[contains(@class, 'fc-prepend-input')]"));
    }

    public SelenideElement stateDd() {
        return $(By.xpath("//div[text()='Current State']/following-sibling::*/div/div[2]/button"));
    }

    public String stateVal() {
        return $(By.xpath("//div[text()='Current State']/following-sibling::*/div/div[2]/div")).text();
    }

    public SelenideElement issueGCBtn() {
        return $(By.xpath("//span[text()='Issue Gift Card']/.."));
    }

    public SelenideElement availableBalance() {
        return $(By.xpath("//div[text()='Available Balance']/following-sibling::*/span"));
    }

    public String availableBalanceVal() {
        String availableBalVal = availableBalance().text();
        return availableBalVal.substring( 1, availableBalVal.length() );
    }

    public SelenideElement presetValue(String val) {
        return $(By.xpath("//div[text()='" + val + "']"));
    }

    public SelenideElement cancelReasonDd() {
        return $(By.xpath("//label[text()='Cancel Reason']/../following-sibling::*/div/div[2]/button"));
    }

    public SelenideElement yesBtn() {
        return $(By.xpath("//span[contains(text(), 'Yes')]/.."));
    }

    public SelenideElement cancelBtn() {
        return $(By.xpath("//a[text()='Cancel']"));
    }





    //------------------------------ HELPERS ---------------------------------//

    @Step("Get '{1}' parameter value of {0}th gift card on the list")
    public String getGCParamVal(String gcIndex, String paramName) {

        String gcParamVal = "";
        waitForDataToLoad();

        switch (paramName) {
            case "Gift Card Number":
                gcParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[2]")).getText();
                break;
            case "Type":
                gcParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[3]/div/div")).getText();
                break;
            case "Original Balance":
                String originalBalance = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[4]/td/span")).getText();
                gcParamVal = originalBalance.substring(1, originalBalance.length());
                break;
            case "Current Balance":
                String currentBalance = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[5]")).getText();
                gcParamVal = currentBalance.substring(1, currentBalance.length());
                break;
            case "Available Balance":
                String availableBalance = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[6]")).getText();
                gcParamVal = availableBalance.substring(1, availableBalance.length());
                break;
            case "State":
                gcParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[7]/span")).getText();
                break;
            case "Date/Time Created":
                gcParamVal = $(By.xpath("//table[@class='fc-table fc-multi-select-table']/tbody/a[" + gcIndex + "]/td[8]/time")).getText();
                break;
        }

        return gcParamVal;

    }

//    incomplete because of bug
//    @Step("Select on the list a customer with name <{0}>.")
//    public void selectCustomer(String customerName) {
//        setFieldVal( customerNameFld(), customerName );
//        customer().shouldBe(visible).click();
//    }

}
