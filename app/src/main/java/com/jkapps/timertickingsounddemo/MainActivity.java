package com.jkapps.timertickingsounddemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    EditText et_mTime;
    TextView tv_mTime;
    Button btnStartStop;
    TextView tvCountDownTimer;

    public CountDownTimer mCountDownTimer;
    public CountDownTimer oneSecTimer; //hidden
    private boolean mTimerRunning;
    private long mTimeLeftInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_mTime = findViewById(R.id.et_mTime);
        tv_mTime = findViewById(R.id.tv_mTime);
        btnStartStop = findViewById(R.id.btn_start);
        tvCountDownTimer = findViewById(R.id.tv_countDownTimer);

        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et_mTime.setVisibility(EditText.INVISIBLE);
                String mTime = et_mTime.getText().toString();
                tv_mTime.setText("For " + mTime + " min.");
                mTimeLeftInMillis = Long.parseLong(mTime) * 60000;
                if (mTimerRunning) {
                    stopTimer();
                } else {
                    playSoundBell();
                    oneSecTimer();
                }
            }
        });
    }

    private void stopTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        et_mTime.setText("");
        updateCountDownText();
        updateUI();
    }

    private void oneSecTimer() {
        oneSecTimer = new CountDownTimer(1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startTimer();
            }
        }.start();
    }

    private void startTimer() {
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
                playSoundTick();  //속도가 중간에 좀 늘어진다? 시스템이 버벅거리는 듯..
            }

            @Override
            public void onFinish() {
                playSoundBell();
                mTimerRunning = false;
                updateUI();
                et_mTime.setText("");
                tvCountDownTimer.setVisibility(TextView.VISIBLE);
                tvCountDownTimer.setText("Done!");
            }
        }.start();

        mTimerRunning = true;
        updateUI();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        }
        tvCountDownTimer.setText(timeLeftFormatted);
    }

    private void updateUI() {
        if (mTimerRunning) {
            et_mTime.setVisibility(EditText.INVISIBLE);
            tv_mTime.setVisibility(TextView.VISIBLE);
            tvCountDownTimer.setVisibility(TextView.VISIBLE);
            btnStartStop.setText("STOP");
        } else {
            et_mTime.setVisibility(EditText.VISIBLE);
            tv_mTime.setVisibility(TextView.INVISIBLE);
            tvCountDownTimer.setVisibility(TextView.INVISIBLE);
            btnStartStop.setText("START");
        }
    }

    private void playSoundBell() {
        MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.bell);
        mp.start();
    }

    private void playSoundTick() {
        MediaPlayer tSound  = MediaPlayer.create(getApplicationContext(), R.raw.tick);
        tSound.start();
        tSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
    }

}