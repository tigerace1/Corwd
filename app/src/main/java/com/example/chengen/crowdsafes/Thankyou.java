package com.example.chengen.crowdsafes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

public class Thankyou extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thankyou);
        getSupportActionBar().hide();
        ImageButton home = (ImageButton)findViewById(R.id.ibHome);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Thankyou.this,NavigationMenu.class);
                intent.putExtra("count",0);
                startActivity(intent);
                finish();
            }
        });
    }
}
