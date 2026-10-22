import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class ApiTest {
    @Test
    public void e2eTest(){
        //Login
        HashMap<Object,Object> loginPayLoad = new HashMap<>();
        loginPayLoad.put("email","argha.testing@gmail.com");
        loginPayLoad.put("password","Argha@1234");

        Playwright playwright = Playwright.create();
        APIRequestContext apiRequest = playwright.request().newContext();
        APIResponse loginResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayLoad));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());

        String token = JsonPath.read(loginResponse.text(),"$.token");
        System.out.println(token);


        //Create Event
        HashMap<Object,Object> createEventPayload = new HashMap<>();
        createEventPayload.put("category","Conference");
        createEventPayload.put("city","Kolkata");
        createEventPayload.put("description","TEST Description");
        createEventPayload.put("eventDate","2026-10-24T05:58:00.000Z");
        createEventPayload.put("price","1000");
        createEventPayload.put("title","TEST Title");
        createEventPayload.put("totalSeats","500");
        createEventPayload.put("venue","Test Venue");

       APIResponse eventResponse = apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization","Bearer "+token).setData(createEventPayload));

        Assert.assertTrue(eventResponse.ok(),"Create Event API Should succeed");
        int eventId = JsonPath.read(eventResponse.text(),"$.data.id"); //.toString()
        System.out.println(eventId);

        //Get Events
        APIResponse retrieveEvents = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").setQueryParam("limit","12")
                .setHeader("Authorization","Bearer "+token));
        Assert.assertTrue(retrieveEvents.ok(),"Event Retrieval API Should succeed");

        List<Integer> allEventsIds = JsonPath.read(retrieveEvents.text(),"$.data[*].id");
        Assert.assertTrue(allEventsIds.contains(eventId),"Created event should appear in event list");


        //Delete Events
        APIResponse deleteEvent = apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization","Bearer "+token));

        Assert.assertTrue(deleteEvent.ok());

        //Verify Deletion is success
        APIResponse verifyRes = apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").setQueryParam("limit","12")
                        .setHeader("Authorization","Bearer "+token));
        Assert.assertTrue(verifyRes.ok(),"Event Retrieval API Should succeed");

        List<Integer> allEventsIdsAfterDeletion = JsonPath.read(verifyRes.text(),"$.data[*].id");
        Assert.assertFalse(allEventsIdsAfterDeletion.contains(eventId),"Created event should appear in event list");


    }
}
