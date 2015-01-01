package com.MAPit.MAPit_backend;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Text;

import java.util.ArrayList;

/**
 * Userinfo endpoint class we are exposing and this will be used for sending and retrieving data about
 * the kind Username
 */
@Api(
        name = "userInfoEndpoint",
        version = "v1",
        resource = "userInfo_Model",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.shubhashis.example.com",
                ownerName = "backend.myapplication.shubhashis.example.com",
                packagePath = ""
        )
)
public class Userinfo_Endpoint {
    public Response_Messages response_messages;

    @ApiMethod(name = "setUserInfo", path = "setUserInfoPath")
    public Response_Messages setUserInfo(Userinfo_Model userinformation) {
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();

        Entity Userinfo_Kind = new Entity("Userinfo");
        Userinfo_Kind.setProperty("Username", userinformation.getName());
        Userinfo_Kind.setProperty("Mail", userinformation.getMail());
        Userinfo_Kind.setUnindexedProperty("Password", userinformation.getPassword());

        if(userinformation.getMobilephone() != null)
        {
            Userinfo_Kind.setUnindexedProperty("Mobile", userinformation.getMobilephone());
        }
        else
        {
            Userinfo_Kind.setUnindexedProperty("Mobile", "");
        }

        if(userinformation.getImagedata() != null)
        {
            Text image_Data = new Text(userinformation.getMobilephone());
            Userinfo_Kind.setUnindexedProperty("Profilepic", image_Data);
        }
        else
        {
            Userinfo_Kind.setUnindexedProperty("Profilepic", "");
        }

        ArrayList <Userinfo_Model> checkMail = getUserInfo(userinformation);

        if(checkMail.size() > 0)
        {
            response_messages = new Response_Messages();
            response_messages.setMessage(response_messages.Userinfo_creation_duplicate);
        }
        else
        {
            datastore.put(Userinfo_Kind);
            response_messages = new Response_Messages();
            response_messages.setMessage(response_messages.Userinfo_creation_OK);
        }
        return response_messages;
    }


    /* Incomplete method, */
    @ApiMethod(name = "getUserInfo", path = "getUserInfoPath")
    public ArrayList<Userinfo_Model> getUserInfo(Userinfo_Model userinformation) {

        ArrayList <Userinfo_Model> Userinfo_Result = new ArrayList<>();

        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();


        Filter Mail_Filter = new Query.FilterPredicate("Mail", Query.FilterOperator.EQUAL, userinformation.getMail());
        Query Mail_Query = new Query("Userinfo").setFilter(Mail_Filter);

        PreparedQuery queryResult = datastore.prepare(Mail_Query);


        for (Entity result : queryResult.asIterable())
        {
            Userinfo_Model um = new Userinfo_Model();

            um.setName((String)result.getProperty("Username"));
            um.setMail((String)result.getProperty("Mail"));

            Userinfo_Result.add(um);
        }

        return Userinfo_Result;
    }


}