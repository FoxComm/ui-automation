package testdata;

import base.BaseTest;
import org.testng.annotations.DataProvider;

public class TestNGDataProviders extends BaseTest {

    @DataProvider
    public Object[][] styledText() {
        return new Object[][] {
                {"p", "Paragraph"},
                {"h1", "Heading One"},
                {"h2", "Heading Two"},
                {"h3", "Heading Three"},
                {"h4", "Heading Four"},
                {"h5", "Heading Five"},
                {"h6", "Heading Six"},
                {"strong", "Bold Text"},
                {"em", "Italic Text"},
                {"ins", "Underlined Text"},
                {"ul", "UL Bullet Point"},
                {"ol", "OL Point"},
                {"ul", "UL Bullet Point"}
        };
    }

    @DataProvider
    public Object[][] entreesSubcategories() {
        return new Object[][]{ {"POULTRY"}, {"SEAFOOD"}, {"MEAT"}, {"VEGETARIAN"} };
    }

}
