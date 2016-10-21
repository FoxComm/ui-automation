package pages;

import base.BasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;

public class InventoryPage extends BasePage {

    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement onHandFld(String type) {
        return $(xpath("//td[text()='" + type + "']/following-sibling::*[1]/div/input"));
    }

    private SelenideElement warehouseTitle(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']"));
    }

    public SelenideElement onHandQty(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']/following-sibling::*[1]"));
    }

    public SelenideElement holdQty(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']/following-sibling::*[2]"));
    }

    public SelenideElement redervedQty(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']/following-sibling::*[3]"));
    }

    public SelenideElement afsQty(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']/following-sibling::*[4]"));
    }

    public SelenideElement afsCostValue(String warehouse) {
        return $(xpath("//td[text()='" + warehouse + "']/following-sibling::*[5]"));
    }

    public SelenideElement arrowBtn(String direction, String type) {
        SelenideElement arrowBtnElem = null;
        switch (type) {
            case "Sellable":
                arrowBtnElem = $$(xpath("//div[text()='Adjust Quantity']/following-sibling::*//i[@class='icon-chevron-" + direction.toLowerCase() + "']")).get(0);
                break;

            case "Non-sellable":
                arrowBtnElem = $$(xpath("//div[text()='Adjust Quantity']/following-sibling::*//i[@class='icon-chevron-" + direction.toLowerCase() + "']")).get(1);
                break;

            case "Backorder":
                arrowBtnElem = $$(xpath("//div[text()='Adjust Quantity']/following-sibling::*//i[@class='icon-chevron-" + direction.toLowerCase() + "']")).get(2);
                break;

            case "Preorder":
                arrowBtnElem = $$(xpath("//div[text()='Adjust Quantity']/following-sibling::*//i[@class='icon-chevron-" + direction.toLowerCase() + "']")).get(3);
                break;
        }
        return arrowBtnElem;
    }

    private SelenideElement transactionsTab() {
        return $(xpath("//a[text()='Transactions']"));
    }



    //----------------------------------- HELPERS -------------------------------------//

    @Step("Click \"Save\"")
    public void clickSave() {
        jsClick(saveBtn());
        shouldBeEnabled(saveBtn(), "\"Save\" btn doesn't get re-enabled");
        sleep(1000);
    }

    @Step("Expand <{0}> warehouse")
    public void expandWarehouse(String warehouse) {
        click(warehouseTitle(warehouse));
    }

    @Step("Set \"On Hand\" amount of <{0}> items to <{1}>")
    public void setOnHand(String type, String amount) {
        setFieldVal(onHandFld(type), amount);
        shouldHaveValue(onHandFld(type), amount, "Failed to modify amount of " + type + " items on hand");
    }

    @Step("Increase qty of <{0}> items on hand by <{1}>")
    public void increaseOnHand_arrowBtn(String type, int amount) {
        click(onHandFld(type));
        for (int i = 0; i < amount; i++) {
            click(arrowBtn("up", type));
        }
    }

    @Step("Decrease qty of <{0}> items on hand by <{1}>")
    public void decreaseOnHand_arrowBtn(String type, int amount) {
        click(onHandFld(type));
        for (int i = 0; i < amount; i++) {
            click(arrowBtn("down", type));
        }
    }

    @Step("Switch to \"Transactions\" tab")
    public void openTransactions() {
        jsClick(transactionsTab());
        waitForDataToLoad();
    }

}
