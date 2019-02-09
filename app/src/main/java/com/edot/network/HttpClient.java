package com.edot.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * @author Raja Gopal P
 */

public interface HttpClient {

    String LOG_TAG = "NetworkClientLogTag";
    String SECURITY_HEADER_PROPERTY_NAME = "edot-security-header";

    /**
     * makes HtTP GET call to the resource @param url
     * @param url - URL of resource
     * @return true if connection is established, otherwise false
     */
    boolean establishConnection(String url);

    /**
     * makes HtTP POST call to the resource @param url
     * @param url - URL of resource
     * @param params - POST parameters to be appended in the body
     * @return true if connection is established, otherwise false
     */
    boolean establishConnection(String url, HashMap<String, String> params);

    /**
     * makes HtTP POST call to the resource @param url
     * @param url - URL of resource
     * @param params - POST parameters to be appended in the body
     * @param appendSecurityHeader - if true securityHeader has to be appended
     * @return true if connection is established, otherwise false
     */
    boolean establishConnection(String url, HashMap<String, String> params,
                                String key, boolean appendSecurityHeader);

    InputStream getInputStream();

    /**
     * Closes the ongoing HttpConnection
     */
    void closeConnection();
}
