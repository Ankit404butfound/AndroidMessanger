package com.ankit.rajmagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;
import android.widget.Toast;

public class MessageViewActivity extends AppCompatActivity {
    ScrollView scroll;

    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            Toast.makeText(getBaseContext(), "jszhcuub", Toast.LENGTH_LONG).show();
            Thread thread = new Thread() {
                @Override
                public void run() {
                    SystemClock.sleep(300);
                    scroll.fullScroll(View.FOCUS_DOWN);
                }
            };
            thread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);

        scroll = findViewById(R.id.message_area);

        Button send = findViewById(R.id.send);
        send.setOnClickListener(v -> scroll.fullScroll(View.FOCUS_DOWN));

        Button back = findViewById(R.id.back);
        back.setOnClickListener(v -> finish());

        EditText message_input = findViewById(R.id.message_input);
        message_input.setOnFocusChangeListener(focusListener);
        message_input.setOnClickListener(v ->{
                Thread thread = new Thread() {
            @Override
            public void run() {
                SystemClock.sleep(300);
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        };
                thread.start();});

        LinearLayout message_filed = findViewById(R.id.message_field);
        RelativeLayout message_ll;

        for (int i=0; i <= 10; i++){
            if (i % 2 == 0) {
                message_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.message_layout_incoming, null);
            }
            else{
                message_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.message_layout_outgoing, null);
            }

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);

            message_ll.setLayoutParams(params);
            message_filed.addView(message_ll);
        }

        scroll.post(new Runnable() {
            @Override
            public void run() {
                scroll.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}