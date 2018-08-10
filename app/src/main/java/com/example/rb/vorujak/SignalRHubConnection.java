package com.example.rb.vorujak;

import android.content.Context;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

import microsoft.aspnet.signalr.client.Credentials;
import microsoft.aspnet.signalr.client.Platform;
import microsoft.aspnet.signalr.client.SignalRFuture;
import microsoft.aspnet.signalr.client.http.Request;
import microsoft.aspnet.signalr.client.http.android.AndroidPlatformComponent;
import microsoft.aspnet.signalr.client.hubs.HubConnection;
import microsoft.aspnet.signalr.client.hubs.HubProxy;
import microsoft.aspnet.signalr.client.hubs.SubscriptionHandler1;
import microsoft.aspnet.signalr.client.transport.ClientTransport;
import microsoft.aspnet.signalr.client.transport.ServerSentEventsTransport;



public class SignalRHubConnection {
    public static HubConnection mHubConnection;
    public static HubProxy mHubProxy;
    private static Context context;
    public static String mConnectionID;

    public SignalRHubConnection(Context context) {
        this.context = context;
    }
    public static void startSignalR() {


            try {
               Platform.loadPlatformComponent(new AndroidPlatformComponent());
                Credentials credentials = new Credentials() {
                    @Override
                    public void prepareRequest(Request request) {
                        //AppPreferences appPrefs = AppPreferences.getInstance(context);
                        request.addHeader("UserName", "Roy" );
                    }
                };
                mHubConnection = new HubConnection("SignalR Hub Url");
                mHubConnection.setCredentials(credentials);
                mHubProxy = mHubConnection.createHubProxy("SignalR Hub");
                ClientTransport clientTransport = new ServerSentEventsTransport(mHubConnection.getLogger());
                SignalRFuture<Void> signalRFuture = mHubConnection.start(clientTransport);
                signalRFuture.get();
                //set connection id
                mConnectionID = mHubConnection.getConnectionId();
                // To get onLine user list
                mHubProxy.on("onGetOnlineContacts",
                        new SubscriptionHandler1<Object>() {
                            @Override
                            public void run(final Object msg) {
                                try {
                                    Gson gson = new Gson();
                                    String json = gson.toJson(msg);
                                    JSONObject responseObject = new JSONObject(json.toString());
                                    JSONArray jsonArray = responseObject.getJSONArray("messageRecipients");
                                    int sizeOfList = jsonArray.length();
                                    for (int i = 0; i < sizeOfList; i++) {
                                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                                        JSONArray onLineUserList = jsonObject.getJSONArray("TwingleChatGroupIds");
                                        int onLineUserCount = onLineUserList.length();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                        , Object.class);
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

/*private SignalRHubConnection mSignalRHubConnection;
// crete connection with signalR hub
mSignalRHubConnection = new SignalRHubConnection(WelcomeActivity.this);
SignalRHubConnection.startSignalR();*/
