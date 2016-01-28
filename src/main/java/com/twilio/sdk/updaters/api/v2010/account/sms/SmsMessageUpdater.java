package com.twilio.sdk.updaters.api.v2010.account.sms;

import com.twilio.sdk.clients.TwilioRestClient;
import com.twilio.sdk.exceptions.ApiConnectionException;
import com.twilio.sdk.exceptions.ApiException;
import com.twilio.sdk.http.HttpMethod;
import com.twilio.sdk.http.Request;
import com.twilio.sdk.http.Response;
import com.twilio.sdk.resources.RestException;
import com.twilio.sdk.resources.api.v2010.account.sms.SmsMessage;
import com.twilio.sdk.updaters.Updater;

public class SmsMessageUpdater extends Updater<SmsMessage> {
    private final String accountSid;
    private final String sid;
    private String body;

    /**
     * Construct a new SmsMessageUpdater
     * 
     * @param accountSid The account_sid
     * @param sid The sid
     */
    public SmsMessageUpdater(final String accountSid, final String sid) {
        this.accountSid = accountSid;
        this.sid = sid;
    }

    /**
     * The body
     * 
     * @param body The body
     * @return this
     */
    public SmsMessageUpdater setBody(final String body) {
        this.body = body;
        return this;
    }

    /**
     * Make the request to the Twilio API to perform the update
     * 
     * @param client TwilioRestClient with which to make the request
     * @return Updated SmsMessage
     */
    @Override
    public SmsMessage execute(final TwilioRestClient client) {
        Request request = new Request(
            HttpMethod.POST,
            TwilioRestClient.Domains.API,
            "/2010-04-01/Accounts/" + this.accountSid + "/SMS/Messages/" + this.sid + ".json",
            client.getAccountSid()
        );
        
        addPostParams(request);
        Response response = client.request(request);
        
        if (response == null) {
            throw new ApiConnectionException("SmsMessage update failed: Unable to connect to server");
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
        
        return SmsMessage.fromJson(response.getStream(), client.getObjectMapper());
    }

    /**
     * Add the requested post parameters to the Request
     * 
     * @param request Request to add post params to
     */
    private void addPostParams(final Request request) {
        if (body != null) {
            request.addPostParam("Body", body);
        }
    }
}