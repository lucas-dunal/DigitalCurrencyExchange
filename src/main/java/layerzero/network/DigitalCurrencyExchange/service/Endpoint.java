package layerzero.network.DigitalCurrencyExchange.service;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

/*
 * Default webservice class
 */
@Component
@Path("/exchange")
public class Endpoint {
    private static final Logger log = LoggerFactory.getLogger(Endpoint.class);

    private final String alphavantageEffectiveURL;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * This is the default constructor for this api endpoint
     *
     * @param url    alphavantage {@paramref url} with {@paramref symbol}, {@paramref market}, {@paramref apikey} placeholders
     * @param symbol exchange symbol
     * @param market exchange market
     * @param apikey api key
     * @throws Exception if the {@paramref url} is invalid, this constructor should fail and prevent application from starting
     */
    @Autowired
    public Endpoint(@Value("${alphavantage.url}") String url,
                    @Value("${alphavantage.symbol}") String symbol,
                    @Value("${alphavantage.market}") String market,
                    @Value("${alphavantage.apikey}") String apikey) throws URISyntaxException, MalformedURLException {
        this.alphavantageEffectiveURL = new URI(String.format(url, symbol, market, apikey)).toURL().toString();
    }

    /**
     * GETs the exchange data from alphavantage
     *
     * @return returns the exchange data as content-type: application/json, or empty json if something went wrong
     */
    @GET
    @Produces({"application/json"})
    public String get() {
        log.info("GET called");
        ResponseEntity<String> resp = restTemplate.getForEntity(alphavantageEffectiveURL, String.class);
        // in case you need to work with the data, use Lombok to build DTOs
        if (!HttpStatus.OK.equals(resp.getStatusCode()) ) {
            log.error("Auch...", resp.getStatusCode());
            throw new WebApplicationException(Response.Status.NOT_FOUND); // hide the root cause and throw 404 to the client
        }
        log.info("GET returning data");
        log.debug(resp.getBody()); // only in debug
        return resp.getBody();
    }
}
