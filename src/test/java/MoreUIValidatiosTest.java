import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.SplittableRandom;

public class MoreUIValidatiosTest {
    Playwright playwright;
    Browser browser;
    Page page;
    BrowserContext context;
    //Page pageB;
    @BeforeMethod
    public void setup(){
        playwright = Playwright.create();
        //browser = playwright.chromium().launch();
        browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setChannel("chrome").setHeadless(false));

        context = browser.newContext();
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
        page.waitForTimeout(2000);

//        BrowserContext contextB = browser.newContext();
//        pageB = contextB.newPage();
    }

    @Test
    public void ChildWindowHandle(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        System.out.println(childText);
        String emailId = childText.split("at ")[1].split(" ")[0];
        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username: ").inputValue());
    }
}
