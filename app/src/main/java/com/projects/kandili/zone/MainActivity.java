package com.projects.kandili.zone;

import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class MainActivity extends AppCompatActivity {

    private EditText mMessage;
    private Button mSendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mMessage = (EditText) findViewById(R.id.messageInput);
        mSendBtn = (Button) findViewById(R.id.sendMessageBtn);

        final String clientId="dineshkandilidinesh";
        final MqttAndroidClient client = new MqttAndroidClient(MainActivity.this, "tcp://172.16.73.4:1883", clientId);
        try
        {
            IMqttToken token = client.connect();
            token.setActionCallback(new IMqttActionListener()
            {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are successful
                    Toast.makeText(getApplicationContext(),"MQTT Service running in the background",Toast.LENGTH_SHORT).show();
                    final String topic = "dotmatrix";
                    mSendBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String payload = null;
                            payload = mMessage.getText().toString();
                            byte[] encodedPayload = new byte[0];
                            try
                            {
                                encodedPayload = payload.getBytes("UTF-8");
                                MqttMessage message = new MqttMessage(encodedPayload);
                                client.publish(topic, message);
                            }
                            catch (UnsupportedEncodingException | MqttException e) {
                                e.printStackTrace();
                            }

                        }
                    });
                }
                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Toast.makeText(MainActivity.this,"Restart the application",Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch (MqttException e)
        {
            e.printStackTrace();
        }

    }
}
