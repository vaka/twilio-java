package com.twilio.sdk.fetchers.api.v2010.account;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.fetchers.Fetcher;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.IncomingPhoneNumber;

public class IncomingPhoneNumberFetcher extends Fetcher<IncomingPhoneNumber> {
    private final String ownerAccountSid;
    private final String sid;

    /**
     * Construct a new IncomingPhoneNumberFetcher
     * 
     * @param ownerAccountSid The owner_account_sid
     * @param sid Fetch by unique incoming-phone-number Sid
     */
    public IncomingPhoneNumberFetcher(final String ownerAccountSid, final String sid) {
        this.ownerAccountSid = ownerAccountSid;
        this.sid = sid;
    }

    /**
     * Make the request to the Twilio API to perform the fetch
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Fetched IncomingPhoneNumber
     */
    @Override
    public IncomingPhoneNumber execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.GET,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.ownerAccountSid + "/IncomingPhoneNumbers/" + this.sid + ".json",
            client.getAccountSid()
        );
        
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("IncomingPhoneNumber fetch failed: Unable to connect to server");
        } else if (response.getStatusCode() != TwilioRestClient.HTTP_STATUS_CODE_OK) {
            RestException restException = RestException.fromJson(response.getStream(), client.getObjectMapper());
            if (restException == null)
                throw new ApiException("Server Error, no content");
            throw new ApiException(
                restException.getMessage(),
                restException.getCode(),
                restException.getMoreInfo(),
                restException.getStatus(),
                null
            );
        }
        
        return IncomingPhoneNumber.fromJson(response.getStream(), client.getObjectMapper());
    }
}