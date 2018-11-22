package com.ayuan.mobilesafe.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.ayuan.mobilesafe.view.SettingItemView;

public class SetupTwoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_two);

        final SettingItemView siv_sim_bound = (SettingItemView) findViewById(R.id.siv_sim_bound);
        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checked = siv_sim_bound.isChecked();
                siv_sim_bound.setCheck(!checked);
            }
        });

    }
}
