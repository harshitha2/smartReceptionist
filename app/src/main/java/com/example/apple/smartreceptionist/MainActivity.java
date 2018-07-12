package com.example.apple.smartreceptionist;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;


public class MainActivity extends AppCompatActivity  implements View.OnClickListener {
    private Button btn1, btn2,btn3;
    ImageView imageView;
    private StorageReference storageReference;


    TextView tvRecvMsg;
    MqttAndroidClient client;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn1 = findViewById(R.id.button3);
        btn2 = findViewById(R.id.button4);
        btn3=findViewById(R.id.buttonDownload);
        imageView=findViewById(R.id.imageView);
        btn3.setOnClickListener(this);

        storageReference= FirebaseStorage.getInstance().getReference();
        String clientId = MqttClient.generateClientId();
        client = new MqttAndroidClient(getApplicationContext(), "tcp://iot.eclipse.org:1883",
                clientId);

        try {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    //We are connected
                    Log.d("MQTT", "onSuccess");
                    //Toast.makeText(MainActivity.this, "Successfull", Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d("MQTT", "onFailure");

                    //Toast.makeText(MainActivity.this, "Not Successfull", Toast.LENGTH_SHORT).show();

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String topic = "foo/bar6";
                String payload = "0";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }
                finally {

                Intent i = new Intent(getApplicationContext(), Main2Activity.class);
                startActivity(i);
                // setContentView(R.layout.activity_main2);
                }

            }
        });



   // @Override
    //public void onClick(View view) {
        //Toast.makeText(getApplicationContext(), "Access Declined", Toast.LENGTH_LONG).show();
        //setContentView(R.layout.activity_main2);

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String topic = "foo/bar6";
                String payload = "1";
                byte[] encodedPayload = new byte[0];
                try {
                    encodedPayload = payload.getBytes("UTF-8");
                    MqttMessage message = new MqttMessage(encodedPayload);
                    client.publish(topic, message);
                } catch (UnsupportedEncodingException | MqttException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    @Override
    public void onClick(View v) {
        storageReference.child("tech.jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try{
                    URL url=new URL(uri.toString());
                    Picasso.with(getBaseContext()).load(String.valueOf(url)).resize(250,250)
                            .noFade().into(imageView);
                    Toast.makeText(getApplicationContext(),"Image Downloaded",Toast.LENGTH_LONG).show();
                }catch (MalformedURLException e){
                    e.printStackTrace();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image not downloaded",Toast.LENGTH_LONG).show();

            }
        });
    }
}



a
