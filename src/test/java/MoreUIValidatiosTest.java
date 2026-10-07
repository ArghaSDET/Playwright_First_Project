import com.microsoft.playwright.*;
import com.microsoft.playwright.assertions.PlaywrightAssertions;
import com.microsoft.playwright.options.AriaRole;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.SplittableRandom;

import static com.microsoft.playwright.assertions.PlaywrightAssertions.assertThat;

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
        context.tracing().start(new Tracing.StartOptions()
                .setScreenshots(true)
                .setSnapshots(true)
                .setSources(true));
        page = context.newPage();
        page.navigate("https://rahulshettyacademy.com/loginpagePractise/");
        //page.waitForTimeout(2000);

//        BrowserContext contextB = browser.newContext();
//        pageB = contextB.newPage();
    }


    @AfterMethod
    public void tearDown(){
        context.tracing().stop(new Tracing.StopOptions()
                .setPath(Paths.get("trace.zip")));
    }
   /* @Test
    public void ChildWindowHandle(){
        Locator blinkingTexts = page.locator(".blinkingText");
        Page newPage = context.waitForPage(()-> blinkingTexts.first().click());
        newPage.waitForLoadState();
        String childText = newPage.locator(".red").textContent();
        System.out.println(childText);
        String emailId = childText.split("at ")[1].split(" ")[0];
        page.getByLabel("Username:").fill(emailId);
        System.out.println(page.getByLabel("Username: ").inputValue());
    }*/
    @Test
    public void UIControls(){
        Locator userRdBtn = page.getByRole(AriaRole.RADIO, new Page.GetByRoleOptions().setName("User"));
        userRdBtn.click();
        page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Okay")).click();
        userRdBtn.isChecked();

        Locator checkBoxTerms = page.getByRole(AriaRole.CHECKBOX, new Page.GetByRoleOptions().setName("I Agree to the terms and conditions"));
        checkBoxTerms.check();
        Assert.assertTrue(checkBoxTerms.isChecked());

        page.getByRole(AriaRole.COMBOBOX).selectOption("Teacher");
        page.waitForTimeout(3000);
    }
}
