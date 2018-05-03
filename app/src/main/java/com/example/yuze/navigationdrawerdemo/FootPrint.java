package com.example.yuze.navigationdrawerdemo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class FootPrint extends AppCompatActivity {

    private Button addTripBtn;
    private Button selectTripBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foot_print);

        addTripBtn = findViewById(R.id.add_trip);
        selectTripBtn = findViewById(R.id.select_trip);

        addTripBtn.setOnClickListener(m_footprint_listener);
        selectTripBtn.setOnClickListener(m_footprint_listener);
    }

    View.OnClickListener m_footprint_listener = v -> {
        switch (v.getId()){
            case R.id.add_trip:
                Intent intent = new Intent(this,NewFootPrint.class);
                startActivity(intent);
                break;
            case R.id.select_trip:
                break;
        }
    };
}
