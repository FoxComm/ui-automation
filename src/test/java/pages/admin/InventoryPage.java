package pages.admin;

import base.AdminBasePage;
import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class InventoryPage extends AdminBasePage {

    //----------------------------------- ELEMENTS -------------------------------------//

    public SelenideElement onHandFld(String type) {
        type = type.replaceAll(" ", "-").toLowerCase();
        return $(xpath("//*[@id='" + type + "']//input"));
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

    public SelenideElement arrowBtn(String type, String action) {
        type = type.toLowerCase();
        action = action.toLowerCase();
        return $(xpath("//*[@id='" + type + "-counter']//i[contains(@class, '" + action + "')]/.."));
    }

    private SelenideElement transactionsTab() {
        return $(xpath("//a[text()='Transactions']"));
    }


    //TODO: Add IDs to Transactions table after bug about it will be fixed


    //----------------------------------- HELPERS -------------------------------------//

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
            click(arrowBtn(type, "up"));
        }
    }

    @Step("Decrease qty of <{0}> items on hand by <{1}>")
    public void decreaseOnHand_arrowBtn(String type, int amount) {
        click(onHandFld(type));
        for (int i = 0; i < amount; i++) {
            click(arrowBtn(type, "down"));
        }
    }

    @Step("Switch to \"Transactions\" tab")
    public void openTransactions() {
        jsClick(transactionsTab());
        waitForDataToLoad();
    }

}
