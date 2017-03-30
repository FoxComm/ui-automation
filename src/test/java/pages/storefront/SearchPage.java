package pages.storefront;

import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.yandex.qatools.allure.annotations.Step;

import static com.codeborne.selenide.Selenide.$;
import static org.openqa.selenium.By.xpath;

public class SearchPage extends GiftCardPdpPage {

    //-------------------------------------------- ELEMENTS --------------------------------------------

    private SelenideElement searchIcon() {
        return $(xpath("//div[contains(@class, 'header')]//span[contains(@class, 'magnifying-glass')]"));
    }


    private SelenideElement searchFld_storefront() {
        return $(xpath("//div[contains(@class, 'header')]//input[@type='search']"));
    }


    //---------------------------------------------- STEPS ---------------------------------------------

    @Step("Click search icon")
    public void clickSearchIcon() {
        click(searchIcon());
    }

    @Step("Set search fld val to <{0}> and press ENTER")
    public void submitSearchQuery(String query) {
        setFieldVal(searchFld_storefront(), query);
        searchFld_storefront().sendKeys(Keys.RETURN);
    }

}