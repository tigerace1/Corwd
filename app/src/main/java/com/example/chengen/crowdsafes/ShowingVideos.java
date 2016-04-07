package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.VideoView;

public class ShowingVideos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showing_videos);
        VideoView vid = (VideoView)findViewById(R.id.videoView);
        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        Uri video = Uri.parse(b.get("VideoData")+"");
        vid.setMediaController(new MediaController(this));
        vid.setVideoURI(video);
        vid.start();
        vid.requestFocus();
    }
}
