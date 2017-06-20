package pages.admin;

import com.codeborne.selenide.SelenideElement;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;
import static org.openqa.selenium.By.xpath;
import static org.testng.Assert.assertTrue;

public class OrderDetailsPage extends CartPage {

    //------------------- G E N E R A L    C O N T R O L S -------------------//
    //------------------------------------------------------------------------//
    //------------------------------ ELEMENTS --------------------------------//

    private SelenideElement orderStateDd() {
        return $(xpath("//div[@id='fct-order-state-dd']"));
    }

    public SelenideElement addTimeBtn() {
        return $(xpath("//button[@id='fct-remorse-timer-extend-btn']"));
    }

    public SelenideElement timer() {
        return $(xpath("//*[contains(@class,'countdown')]"));
    }

    //------------------------------- HELPERS --------------------------------//

    @Step("Click \"Add Remorse Time\" btn <{0}> times")
    public void clickAddTimeBtn_times(int times) {
        for (int i = 0; i < times; i++) {
            click(addTimeBtn());
            sleep(100);
        }
    }

    @Step("Change order state to {0}")
    public void setOrderState(String state) {
        setDdVal(orderStateDd(), state);
        click(yesBtn());
    }

    @Step("Check if remorse hold time has been increased.")
    public void assertTimerValue(int expectedHoursVal, int expectedMinutesVal) {

        shouldBeVisible(timer(), "Timer isn't visible");
        String timerVal = timer().text();
        Integer actualMinutesVal = Integer.valueOf(timerVal.substring(3, 5));
        Integer actualHoursVal = Integer.valueOf(timerVal.substring(0, 2));

        assertTrue( (expectedHoursVal == actualHoursVal) && (expectedMinutesVal - actualMinutesVal <= 1),
                "Actual 'Remorse Hold' timer value differs from expected one." );

    }

    @Step("Assert that order's state is '{0}'.")
    public void assertOrderState(String expectedState) {
        orderState().shouldHave(text(expectedState));
    }
}