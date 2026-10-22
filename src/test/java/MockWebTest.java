import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.LocatorAssertions;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class MockWebTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp() {
        playwright = Playwright.create();
        //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/");
        page.setDefaultTimeout(5000); //Set Global Default Timeout //Default timeout is 5 Second
        PlaywrightAssertions.setDefaultAssertionTimeout(7000); //Set Global Assertion Default Timeout //Default timeout is 10 Second
    }

    @Test(description = "Create Event")
    public void DemoTest() {

        page.getByPlaceholder("you@email.com").fill("argha.testing@gmail.com");
        page.getByLabel("Password").fill("Argha@1234");
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

        //Create Event
        assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

        page.route("**/api/events**", route -> route.fulfill(
                new Route.FulfillOptions().setPath(Paths.get("src/test/resources/events_2.json"))
                ));




        page.navigate("https://eventhub.rahulshettyacademy.com/events");
        page.waitForTimeout(7000);
    }

}