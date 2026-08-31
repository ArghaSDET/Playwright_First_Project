import com.microsoft.playwright.*;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

public class BasicsTest {
    Playwright playwright;
    Browser browser;
    Page page;

    @BeforeMethod
    public void setUp(){
        playwright = Playwright.create();
        //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.firefox().launch(new BrowserType.LaunchOptions().setHeadless(false));
        //Browser browser = playwright.webkit().launch(new BrowserType.LaunchOptions().setHeadless(false));

        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));
        page = browser.newPage();
        page.navigate("https://eventhub.rahulshettyacademy.com/");
    }

    @Test(description = "Create Event")
    public void DemoTest(){

       System.out.println(page.title());
       assertThat(page).hasTitle("EventHub — Discover & Book Events");

       page.getByPlaceholder("you@email.com").fill("argha.testing@gmail.com");
       page.getByLabel("Password").fill("Argha@1234");
       page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Sign In")).click();

       assertThat(page.getByRole(AriaRole.LINK, new Page.GetByRoleOptions().setName("Browse Events →"))).isVisible();

       page.navigate("https://eventhub.rahulshettyacademy.com/admin/events");
       page.locator("#event-title-input").fill("Test 7");
       page.locator("#admin-event-form textarea").fill("Test Event");
       page.getByLabel("Category").selectOption("Concert");
       page.getByLabel("City").fill("Kolkata");
       page.getByLabel("Venue").fill("Test Venue");
       page.getByLabel("Event Date & Time").fill("2026-12-10T08:00");
       //page.waitForTimeout(6000);
       //page.pause();

       page.getByLabel("Price ($)").fill("100");
       page.getByLabel("Total Seats").fill("1000");
       page.locator("#add-event-btn").click();

       assertThat(page.getByText("Event created!")).isVisible();
       page.locator("#nav-events").click();
       // page.waitForTimeout(2000);
       Locator eventCards = page.getByTestId("event-card");
       System.out.println(eventCards.count());
       Locator targetCard = eventCards.filter(new Locator.FilterOptions().setHasText("Test 3"));
       assertThat(targetCard).isVisible();







    }

    @AfterMethod
    public void tearDown(){
        //Test
    }
}