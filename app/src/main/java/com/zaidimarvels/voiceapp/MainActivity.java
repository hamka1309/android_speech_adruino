package com.zaidimarvels.voiceapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;


import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Locale;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    public static final int REQ_CODE = 100;
    public final String ON_DOOR = "1";
    public final String OFF_DOOR = "2";
    public final String ON_FAN = "60";
    public final String OFF_FAN = "61";
    public final String ON_LED_LIV = "10";
    public final String OFF_LED_LIV = "11";
    public final String ON_LED_BED = "20";
    public final String OFF_LED_BED = "21";
    public final String ON_LED_BATH = "30";
    public final String OFF_LED_BATH = "31";
    public final String ON_LED_GAR = "40";
    public final String OFF_LED_GAR = "41";
    public final String ON_LED_OUT = "50";
    public final String OFF_LED_OUT = "51";

    @BindView(R.id.iv_fan)
    ImageView ivFan;
    @BindView(R.id.iv_auto_door)
    ImageView ivAutoDoor;
    @BindView(R.id.iv_led)
    ImageView ivLed;
    @BindView(R.id.layout_fan)
    View layoutFan;
    @BindView(R.id.layout_gara)
    View layoutGara;
    @BindView(R.id.layout_led)
    View layoutLed;
    @BindView(R.id.swt_door_gara)
    SwitchCompat swtDoorGara;
    @BindView(R.id.swt_led_gara)
    SwitchCompat swtLedGara;
    @BindView(R.id.swt_fan)
    SwitchCompat swtFan;
    @BindView(R.id.swt_led_bath)
    SwitchCompat swtLedBath;
    @BindView(R.id.swt_led_bed)
    SwitchCompat swtLedBed;
    @BindView(R.id.swt_led_liv)
    SwitchCompat swtLedLiv;
    @BindView(R.id.swt_led_out)
    SwitchCompat swtLedOut;
    @BindView(R.id.bt_connect)
    Button btConnect;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private BluetoothSPP bluetooth;
    private TextToSpeech tts;
    private SpeechRecognizer speechRecog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        layoutFan.setVisibility(View.GONE);
        layoutGara.setVisibility(View.GONE);
        layoutLed.setVisibility(View.GONE);
        bluetooth = new BluetoothSPP(this);

        if (!bluetooth.isBluetoothAvailable()) {
            Toast.makeText(getApplicationContext(), "Bluetooth is not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        bluetooth.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() {
            public void onDeviceConnected(String name, String address) {
                btConnect.setText("Connected to " + name);
            }

            public void onDeviceDisconnected() {
                btConnect.setText("Connection lost");
            }

            public void onDeviceConnectionFailed() {
                btConnect.setText("Unable to connect");
            }
        });

        btConnect.setOnClickListener(v -> {
            if (bluetooth.getServiceState() == BluetoothState.STATE_CONNECTED) {
                bluetooth.disconnect();
            } else {
                Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
            }
        });
        onClickSwitch();

        fab.setOnClickListener(view -> {

            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, R.string.voice_need_to_speak);
            try {
                this.startActivityForResult(intent, REQ_CODE);
            } catch (ActivityNotFoundException a) {
                Toast.makeText(this, getResources().getString(R.string.no_support_voice), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void onResultVoid(String result_message) {
        result_message = result_message.toLowerCase();
        Toast.makeText(this, result_message, Toast.LENGTH_SHORT).show();
        if (result_message.equalsIgnoreCase("turn on led gara")) {
            bluetooth.send(ON_LED_GAR, true);
        } else if (result_message.equalsIgnoreCase("turn off led gara")) {
            bluetooth.send(OFF_LED_GAR, true);
        } else if (result_message.equalsIgnoreCase("turn on led bed")) {
            bluetooth.send(ON_LED_BED, true);
        } else if (result_message.equalsIgnoreCase("turn off led bed")) {
            bluetooth.send(OFF_LED_BED, true);
        } else if (result_message.equalsIgnoreCase("turn on led bath")) {
            bluetooth.send(ON_LED_BATH, true);
        } else if (result_message.equalsIgnoreCase("turn off led bath")) {
            bluetooth.send(OFF_LED_BATH, true);
        } else if (result_message.equalsIgnoreCase("turn on led liv")) {
            bluetooth.send(ON_LED_LIV, true);
        } else if (result_message.equalsIgnoreCase("turn off led liv")) {
            bluetooth.send(OFF_LED_LIV, true);
        } else if (result_message.equalsIgnoreCase("turn on led out")) {
            bluetooth.send(ON_LED_OUT, true);
        } else if (result_message.equalsIgnoreCase("turn off led out")) {
            bluetooth.send(OFF_LED_OUT, true);
        } else if (result_message.equalsIgnoreCase("turn on door")) {
            bluetooth.send(ON_DOOR, true);
        } else if (result_message.equalsIgnoreCase("turn off door")) {
            bluetooth.send(OFF_DOOR, true);
        } else if (result_message.equalsIgnoreCase("turn on fan")) {
            Log.e("ha", "processResult: ");
            bluetooth.send(ON_FAN, true);
        } else if (result_message.equalsIgnoreCase("turn off fan")) {
            bluetooth.send(OFF_FAN, true);
        }
    }

    public void onStart() {
        super.onStart();
        if (!bluetooth.isBluetoothEnabled()) {
            bluetooth.enable();
        } else {
            if (!bluetooth.isServiceAvailable()) {
                bluetooth.setupService();
                bluetooth.startService(BluetoothState.DEVICE_OTHER);
            }
        }
    }

    public void onDestroy() {
        super.onDestroy();
        bluetooth.stopService();
    }

    @OnClick({R.id.iv_led, R.id.iv_fan, R.id.iv_auto_door})
    public void onClickMenu(View view) {
        switch (view.getId()) {
            case R.id.iv_auto_door:
                layoutGara.setVisibility(View.VISIBLE);
                layoutFan.setVisibility(View.GONE);
                layoutLed.setVisibility(View.GONE);
                break;
            case R.id.iv_fan:
                layoutGara.setVisibility(View.GONE);
                layoutFan.setVisibility(View.VISIBLE);
                layoutLed.setVisibility(View.GONE);
                break;
            case R.id.iv_led:
                layoutGara.setVisibility(View.GONE);
                layoutFan.setVisibility(View.GONE);
                layoutLed.setVisibility(View.VISIBLE);
                break;
        }
    }

    public void onClickSwitch() {
        this.swtFan.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_FAN, true);
            } else {
                bluetooth.send(OFF_FAN, true);
            }
        });

        this.swtDoorGara.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_DOOR, true);
            } else {
                bluetooth.send(OFF_DOOR, true);
            }
        });
        this.swtLedGara.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_GAR, true);
            } else {
                bluetooth.send(OFF_LED_GAR, true);
            }
        });
        this.swtLedLiv.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_LIV, true);
            } else {
                bluetooth.send(OFF_LED_LIV, true);
            }
        });
        this.swtLedBath.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_BATH, true);
            } else {
                bluetooth.send(OFF_LED_BATH, true);
            }
        });
        this.swtLedBed.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_BED, true);
            } else {
                bluetooth.send(OFF_LED_BED, true);
            }
        });
        this.swtLedOut.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_OUT, true);
            } else {
                bluetooth.send(OFF_LED_OUT, true);
            }
        });
        this.swtDoorGara.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                bluetooth.send(ON_LED_GAR, true);
            } else {
                bluetooth.send(OFF_LED_GAR, true);
            }
        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bluetooth.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bluetooth.setupService();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        } else if (requestCode == REQ_CODE) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                this.onResultVoid(result.get(0).toString());
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
