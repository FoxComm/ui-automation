package pages;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;
import ru.yandex.qatools.allure.annotations.Step;

import java.util.Objects;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static org.testng.Assert.assertTrue;

public class OrderDetailsPage extends CartPage {

    //------------------- G E N E R A L    C O N T R O L S -------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement orderStateDd() {
        return $(By.xpath("//div[text()='Order State']/following-sibling::*/div/div[2]/button"));
    }

    public SelenideElement addTimeBtn() {
        return $(By.xpath("//button[@class='fc-btn fc-remorse-timer-extend']"));
    }

    public SelenideElement timer() {
        return $(By.xpath("//div[@class='fc-countdown']"));
    }

    //------------------------------- HELPERS --------------------------------//

    @Step("Set order state to {0}.")
    public void setOrderState(String state) {
        click( orderStateDd() );
        click( $(By.xpath("//li[text()='" + state + "']")) );
        click( $(By.xpath("//span[text()='Yes, Change']/..")) );
    }

    @Step("Check if remorse hold time has been increased.")
    public void assertTimerValue(int expectedHoursVal, int expectedMinutesVal) {

        int actualMinutesVal = Integer.valueOf(timer().val().substring(3, 5));
        int actualHoursVal = Integer.valueOf(timer().val().substring(0, 2));

        System.out.println("actualMinutesVal: " + actualMinutesVal);
        System.out.println("actualHoursVal: " + actualHoursVal);

        assertTrue( (expectedHoursVal == actualHoursVal) && (expectedMinutesVal - actualMinutesVal <= 1),
                "Actual 'Remorse Hold' timer value differs from expected one." );

    }

    @Step("Assert that order's state is '{0}'.")
    public void assertOrderState(String expectedState) {

        System.out.println(orderOverviewPanel().waitUntil(visible, 10000));

        if (Objects.equals( expectedState, "Remorse Hold" ) ||
            Objects.equals( expectedState, "Manual Hold" ) ||
            Objects.equals( expectedState, "Fraud Hold" )) {

            $(By.xpath("//div[text()='" + expectedState + "']")).shouldBe(visible
                    .because("Order is not on " + expectedState + "."));

        } else if (Objects.equals( expectedState, "Fulfillment Started" ) ||
                    Objects.equals( expectedState, "Canceled" )) {

            $(By.xpath("//div[@class=' fc-panel-list']/div[1]/div/span[text()='" + expectedState + "']")).shouldBe(visible
                    .because("Order is not in '" + expectedState + "' state"));

        }

    }

}