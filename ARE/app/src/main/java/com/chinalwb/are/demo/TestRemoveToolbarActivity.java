package com.chinalwb.are.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;

import com.chinalwb.are.AREditText;

public class TestRemoveToolbarActivity extends AppCompatActivity {
    private AREditText arEditText;

    private Button btnBold;
    private Button btnBoldAll;
    private Button btnAlignmentLeft;
    private Button btnAlignmentCenter;
    private Button btnAlignmentRight;
    private Button btnSetColor;
    private Button btnSetAllColor;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_remove_toolbar);
        arEditText = findViewById(R.id.arEditText);
        btnBold = findViewById(R.id.btnBold);
        btnBoldAll = findViewById(R.id.btnBoldAll);
        btnAlignmentLeft = findViewById(R.id.btnAlignmentLeft);
        btnAlignmentCenter = findViewById(R.id.btnAlignmentCenter);
        btnAlignmentRight = findViewById(R.id.btnAlignmentRight);
        btnSetColor = findViewById(R.id.btnSetColor);
        btnSetAllColor = findViewById(R.id.btnSetAllColor);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String html = arEditText.getHtml();
                arEditText.setText(arEditText.getEditableText().subSequence(0, 5));
                Log.i("wk", "保存结果：");
                Log.i("wk", html);
            }
        });
        btnBold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.bold();
            }
        });
        btnBoldAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.boldAll();
            }
        });
        btnAlignmentLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.alignmentLeft();
            }
        });
        btnAlignmentCenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.alignmentCenter();
            }
        });
        btnAlignmentRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.alignmentRight();
            }
        });
        btnSetColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.color(Color.RED);
            }
        });
        btnSetAllColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arEditText.colorAll(Color.GREEN);
            }
        });
    }
}