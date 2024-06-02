package layerzero.network.DigitalCurrencyExchange.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/*
 * Default test for the webservice
 */
@ExtendWith(MockitoExtension.class)
class EndpointTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private Endpoint service = new Endpoint("https://google.com", null, null, null);

    EndpointTest() throws MalformedURLException, URISyntaxException {
    }

    /**
     * Test that the logic of the service returns the data directly without modifications
     */
    @Test
    void get() {
        String in = "{\n" +
                "  \"Meta Data\": {\n" +
                "    \"1. Information\": \"Daily Prices and Volumes for Digital Currency\",\n" +
                "    \"2. Digital Currency Code\": \"BTC\",\n" +
                "    \"3. Digital Currency Name\": \"Bitcoin\",\n" +
                "    \"4. Market Code\": \"EUR\",\n" +
                "    \"5. Market Name\": \"Euro\",\n" +
                "    \"6. Last Refreshed\": \"2024-05-31 00:00:00\",\n" +
                "    \"7. Time Zone\": \"UTC\"\n" +
                "  },\n" +
                "  \"Time Series (Digital Currency Daily)\": {\n" +
                "    \"2023-08-06\": {\n" +
                "      \"1. open\": \"26411.90000000\",\n" +
                "      \"2. high\": \"26480.79000000\",\n" +
                "      \"3. low\": \"26325.26000000\",\n" +
                "      \"4. close\": \"26395.93000000\",\n" +
                "      \"5. volume\": \"60.04654686\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        Mockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.any()))
                .thenReturn(new ResponseEntity(in, HttpStatus.OK));

        final String out = service.get();
        Assertions.assertNotEquals("{ bazinga }", out);
        Assertions.assertEquals(in, out);
    }
}