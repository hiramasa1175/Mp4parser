package com.example.mp4parser;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Parser.append();
        Parser.test();

        VideoView videoView = findViewById(R.id.videoView);
        videoView.setVideoPath(Environment.getExternalStorageDirectory().toString() + "/Movies/output2.mp4");
        videoView.start();
        videoView.setMediaController(new MyMediaController(this));
    }
}
