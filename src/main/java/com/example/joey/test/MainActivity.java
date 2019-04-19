package com.example.joey.test;

import android.content.Intent;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    TextToSpeech mTTS;
    private SeekBar msbPitch;
    private SeekBar msbSpeed;
    Spinner langSelect;

    Locale[] locales = Locale.getAvailableLocales();
    ArrayAdapter <String> adapter;
    List<Locale> localeList=new ArrayList<Locale>();
    List<String> spinnerArray=new ArrayList<String>();
    int sum=0;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i==TextToSpeech.SUCCESS){
                    TTSinitialized();

                }

            }
        });

    }
    private void TTSinitialized(){
        startService(new Intent(this, TTSService.class));
        for (Locale locale : locales) {
            int res = mTTS.isLanguageAvailable(locale);
            if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                sum++;
                localeList.add(locale);
                spinnerArray.add(locale.getDisplayName());

            }
        }
        msbPitch=findViewById(R.id.seek_bar_pitch);
        msbSpeed=findViewById(R.id.seek_bar_speed);
        langSelect=findViewById(R.id.spinner);
        langSelect.setOnItemSelectedListener(this);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        langSelect.setAdapter(adapter);
        langSelect.setOnItemSelectedListener(this);
    }

    @Override
    protected void onDestroy() {
        if (mTTS!=null) {
            mTTS.stop();
            mTTS.shutdown();
        }
        stopService(new Intent(this, TTSService.class));
        super.onDestroy();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        float pitch=(float)msbPitch.getProgress()/50;
        if (pitch<0.1) pitch=0.1f;
        float speed=(float)msbSpeed.getProgress()/50;
        if (speed<0.1) speed=0.1f;
        TTSService.setLanguage(localeList.get(i),pitch,speed);
        Toast.makeText(getBaseContext(),i+"is selected",Toast.LENGTH_LONG).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
