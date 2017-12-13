package com.example.peter.detailpost;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        Button btn = findViewById(R.id.btnXem);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Main2Activity.this,MainActivity.class);
                intent.putExtra("uidPost","cKKoL4B1DyZY4V6FBDQmL86cpil2");
                intent.putExtra("keyPost","-L0DtXk05DiPubF5UU2d");
                intent.putExtra("nickName","Peter Tu");
                startActivity(intent);
            }
        });

    }
}
