package com.example.joey.test;

import android.app.Service;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

public class TTSService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private static TextToSpeech mTTS;
    static ClipboardManager clipboard;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        clipboard = (ClipboardManager) this.getSystemService(CLIPBOARD_SERVICE);
        clipboard.addPrimaryClipChangedListener(mPrimaryChangeListener);
        mTTS = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {

            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    ClipboardManager.OnPrimaryClipChangedListener mPrimaryChangeListener = new ClipboardManager.OnPrimaryClipChangedListener() {
        public void onPrimaryClipChanged() {
            speak();
        }
    };

    public static void setLanguage(Locale lang, float pitch, float speed){
        mTTS.setLanguage(lang);
        mTTS.setPitch(pitch);
        mTTS.setSpeechRate(speed);
    }

    public static void speak(){
        ClipData clipData = clipboard.getPrimaryClip();
        String text = clipData.getItemAt(0).getText().toString();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mTTS.speak(text,TextToSpeech.QUEUE_FLUSH,null,null);
        } else {
            mTTS.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

}
