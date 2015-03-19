package org.asuki.webservice;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;

public class ReCaptchaService {
    private static final String RECAPTCHA_URL = "http://www.google.com/recaptcha/api/verify";
    private static final String RECAPTCHA_PRIVATE_KEY = "6LdjBeMSAAAAAB8HeTU0ihREtwJyIiKUqK6x-6pL";

    public boolean verify(String challenge, String response) {
        ClientRequest request = new ClientRequest(RECAPTCHA_URL);

        request.setHttpMethod("GET");

        request.queryParameter("privatekey", RECAPTCHA_PRIVATE_KEY)
                .queryParameter("remoteip", "localhost")
                .queryParameter("challenge", challenge)
                .queryParameter("response", response);

        ClientResponse<String> serviceResponse;

        try {
            serviceResponse = request.get(String.class);
        } catch (Exception e) {
            throw new RuntimeException("Could not verify captcha.", e);
        }

        if (200 == serviceResponse.getStatus()) {
            return serviceResponse.getEntity(String.class).contains("true");
        }

        return false;
    }

}
