package com.e.driver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.pubnub.api.PNConfiguration;
import com.pubnub.api.PubNub;
import com.pubnub.api.callbacks.PNCallback;
import com.pubnub.api.models.consumer.PNPublishResult;
import com.pubnub.api.models.consumer.PNStatus;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    Button btnSend;
    PubNub pubnub;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSend = findViewById(R.id.button);
        publish();
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                JsonObject position = new JsonObject();
                position.addProperty("lat", 32L);
                position.addProperty("lng", 32L);


                System.out.println("before pub: " + position);
                if (pubnub != null) {

                    pubnub.publish()
                            .message(position)
                            .channel("my_channel")
                            .async(new PNCallback<PNPublishResult>() {
                                @Override
                                public void onResponse(PNPublishResult result, PNStatus status) {

                                    if (!status.isError()) {
                                        System.out.println("pub timetoken: " + result.getTimetoken());
                                    }
                                    System.out.println("pub status code: " + status.getStatusCode());
                                }
                            });
                } else {
                    Toast.makeText(MainActivity.this, "mssage not send", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void publish() {

        PNConfiguration pnConfiguration = new PNConfiguration();
        pnConfiguration.setPublishKey("pub-c-739ae71e-8682-47e6-b624-6dcbd4730016");
        pnConfiguration.setSubscribeKey("sub-c-e422c910-5048-11e9-b0df-968893e54af3");
        pnConfiguration.setSecure(false);

        pubnub = new PubNub(pnConfiguration);


        pubnub.subscribe()
                .channels(Arrays.asList("my_channel"))
                .execute();
    }

}