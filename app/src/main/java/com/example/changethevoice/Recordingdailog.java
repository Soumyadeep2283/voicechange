package com.example.changethevoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

public class Recordingdailog extends AppCompatActivity implements Animation.AnimationListener {

    ImageView imageView;
    TextView localbutton;
    Animation animation;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recordingdailog);


        imageView = findViewById(R.id.imgLogo);
        localbutton = findViewById(R.id.btn_cancel);

        animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.boom_out);
        animation.setAnimationListener(this);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(this.animation);

        localbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                VoiceActivity.reccording = false ;
                Recordingdailog.this.finish();
            }
        });





    }

    @Override
    public void onAnimationStart(Animation animation) {
        imageView.startAnimation(this.animation); //Latest add
    }

    @Override
    public void onAnimationEnd(Animation animation) {
        imageView.startAnimation(this.animation); //Latest add
    }

    @Override
    public void onAnimationRepeat(Animation animation) {
        imageView.startAnimation(this.animation); //Latest add
    }
}