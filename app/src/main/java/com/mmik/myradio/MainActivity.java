package com.mmik.myradio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.IOException;

/**
 * @author mmik
 */
public class MainActivity extends AppCompatActivity {

    // Variables
    boolean prepared=false, started=false, paused=false;

    // Components
    private Button butPlayer;
    private Button butStop;

//    private String STREAM_URL = "http://69.46.78.180:80/";
    private String STREAM_URL = "http://stream.radioreklama.bg:80/aubg-radio";
    private MediaPlayer mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Built View components
        butPlayer = (Button)findViewById(R.id.but_play);
        butPlayer.setEnabled(false);
        butPlayer.setText(R.string.msg_loading);
        butStop = (Button)findViewById(R.id.but_stop);

        // Media player
        mPlayer = new MediaPlayer();
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new PlayerTask().execute(STREAM_URL);

        // Handle action commands
        butPlayer.setOnClickListener(onClickPlayer());
        butStop.setOnClickListener(onClickStop());

    }

    /**
     * Click listener on PLAY button
     * @return View.OnClickListener
     */
    @NonNull
    private View.OnClickListener onClickPlayer() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started){
                    started=false;
                    mPlayer.pause();
                    butPlayer.setText(R.string.txt_play);
                }else {
                    started=true;
                    mPlayer.start();
                    butPlayer.setText(R.string.txt_pause);
                }
            }
        };
    }

    /**
     * Click listener on STOP button
     * @return View.OnClickListener
     */
    @NonNull
    private View.OnClickListener onClickStop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(started){
                    started=false;
                    mPlayer.stop();
                    butPlayer.setText(R.string.txt_play);
                }
                if(paused){
                    paused=false;
                }
                if(prepared){
                    mPlayer.release();
                }
            }
        };
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(started){
            mPlayer.pause();
            paused=true;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(started){
            mPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(prepared){
            mPlayer.release();
        }
    }

    class PlayerTask extends AsyncTask<String, Void, Boolean>{
        @Override
        protected Boolean doInBackground(String... params) {
            try {
                mPlayer.setDataSource(params[0]);
                mPlayer.prepare();
                prepared=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return prepared;
        }

        @Override
        protected void onPostExecute(Boolean bPrepared){
            super.onPostExecute(bPrepared);
            butPlayer.setEnabled(true);
            butPlayer.setText(R.string.txt_play);
        }
    }
}
