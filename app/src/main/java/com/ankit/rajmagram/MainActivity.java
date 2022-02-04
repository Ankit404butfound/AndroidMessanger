package com.ankit.rajmagram;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.fonts.FontFamily;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    EditText mTextView;
    String txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout master_ll = findViewById(R.id.user_field_layout);



        for (int i=0; i <= 20; i++){
            RelativeLayout child_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.user_area, null);
            child_ll.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, MessageViewActivity.class);
                startActivity(intent);
            });
            View viewDivider = new View(this);
            viewDivider.setBackgroundColor(Color.parseColor("#e9edef"));
            int dividerHeight = (int) (getResources().getDisplayMetrics().density * 1); // 1dp to pixels
            viewDivider.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dividerHeight));

            TextView user_details = child_ll.findViewById(R.id.user_details);

            String message = "Hello how are you there?";

            String boldText = "Ankit Raj";
            String normalText;
            if (message.length() >= 20) {
                normalText = "\n" + message.substring(0, 20) + "...";
            }
            else{
                normalText = "\n" + message.toString();
            }
            SpannableString str = new SpannableString(boldText + normalText);
            str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            str.setSpan(new RelativeSizeSpan(1.2f), 0, boldText.length(), 0);
            str.setSpan(new ForegroundColorSpan(Color.parseColor("#141b21")), 0, boldText.length(), 0);
            user_details.setText(str);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 10, 0, 10);

            child_ll.setLayoutParams(params);

            master_ll.addView(child_ll);
            master_ll.addView(viewDivider);
        }

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(v -> Toast.makeText(this, "jszhcuub", Toast.LENGTH_LONG).show());
    }

    private class MySyncTask extends AsyncTask {
        @Override
        protected Object doInBackground(Object[] objects) {
            int counter = 0;
            while (true) {
                counter++;
                SystemClock.sleep(1000);
                publishProgress(counter);
            }
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            if (values.length > 0) {
                txt = txt+"\n"+ values[0];
                mTextView.setText(txt);
            }
        }
    }
}