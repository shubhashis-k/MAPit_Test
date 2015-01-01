package com.MAPit.MAPit_backend;

/**
 * Created by shubhashis on 12/27/2014.
 */
public class Response_Messages {
    public String response_message;

    public final String Userinfo_creation_OK = "OK";
    public final String Userinfo_creation_duplicate = "DUPLICATE";

    public void setMessage(String message)
    {
        response_message = message;
    }

    public String getMessage()
    {
        return response_message;
    }
}
