package com.ankit.rajmagram;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class CustomDialog extends Dialog {
    public Activity activity;
    EditText global_username;
    JSONObject jsonResponse = new JSONObject();
    public Button search;
    AsyncTask<String, String, String> task;
    TextView username_hint;

    class retrievedata extends AsyncTask<String, String, String> {
        retrievedata() {
        }

        /* Access modifiers changed, original: protected|varargs */
        public String doInBackground(String... arg) {
            URL url = null;
            StringBuffer buffer = null;
            try {
                url = new URL(arg[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            try {
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                if (conn.getResponseCode() != 200) {
                    return buffer.toString();
                }
                try {
                    InputStream in = new BufferedInputStream(conn.getInputStream());
                    InputStream inputStream = conn.getInputStream();
                    buffer = new StringBuffer();
                    if (inputStream == null) {
                        return null;
                    }
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    while (true) {
                        String readLine = reader.readLine();
                        String line = readLine;
                        if (readLine == null) {
                            break;
                        }
                        StringBuilder stringBuilder = new StringBuilder();
                        stringBuilder.append(line);
                        stringBuilder.append("\n");
                        buffer.append(stringBuilder.toString());
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    return buffer.toString();
                } catch (IOException e2) {
                    e2.printStackTrace();
                    return null;
                }
            } catch (IOException e3) {
                e3.printStackTrace();
                return "error";
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String response) {
            if (response == null) {
                return;
            }
            if (response == "error") {
                String hint = "Internet error...";
                CustomDialog.this.jsonResponse = new JSONObject();
                SpannableString str = new SpannableString(hint);
                str.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 0, hint.length(), 0);
                CustomDialog.this.username_hint.setText(str);
                return;
            }
            try {
                JSONObject json = new JSONObject(response);
                Log.d("Tag", json.toString());
                String hint2;
                SpannableString str2;
                if (json.getInt("status") == 200) {
                    CustomDialog.this.jsonResponse = json.getJSONObject("data");
                    hint2 = "Username exists...";
                    str2 = new SpannableString(hint2);
                    str2.setSpan(new ForegroundColorSpan(Color.parseColor("#48a868")), 0, hint2.length(), 0);
                    CustomDialog.this.username_hint.setText(str2);
                    return;
                }
                hint2 = "Username not found...";
                CustomDialog.this.jsonResponse = new JSONObject();
                str2 = new SpannableString(hint2);
                str2.setSpan(new ForegroundColorSpan(Color.parseColor("#c20000")), 0, hint2.length(), 0);
                CustomDialog.this.username_hint.setText(str2);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public CustomDialog(Activity a) {
        super(a);
        this.activity = a;
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(1);
        setContentView(R.layout.custom_dialog);
        this.username_hint = (TextView) findViewById(R.id.username_hint);
        EditText editText = (EditText) findViewById(R.id.global_username);
        this.global_username = editText;
        editText.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    String hint = "Searching...";
                    PrintStream printStream = System.out;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("tessssssssssst");
                    stringBuilder.append(s.toString());
                    printStream.println(stringBuilder.toString());
                    System.out.println(Globals.current_user);
                    System.out.println(Globals.password);
                    SpannableString str = new SpannableString(hint);
                    str.setSpan(new ForegroundColorSpan(Color.parseColor("#04007d")), 0, hint.length(), 0);
                    CustomDialog.this.username_hint.setText(str);
                    try {
                        CustomDialog.this.task.cancel(true);
                    } catch (Exception e) {
                    }
                    CustomDialog.this.task = new retrievedata();
                    CustomDialog.this.task.execute(new String[]{Globals.get_user_url(s.toString())});
                    return;
                }
                CustomDialog.this.username_hint.setText("");
            }

            public void afterTextChanged(Editable s) {
            }
        });
        Button button = (Button) findViewById(R.id.search);
        this.search = button;
        button.setOnClickListener(new -$$Lambda$CustomDialog$VPuCsPDFuwxbA4mYyIJ3gOFsbbU(this));
    }

    public /* synthetic */ void lambda$onCreate$0$CustomDialog(View v) {
        try {
            Globals.current_receiver = this.jsonResponse.getString("username");
            Globals.current_receiver_first_name = this.jsonResponse.getString("first_name");
            Globals.current_receiver_last_name = this.jsonResponse.getString("last_name");
            dismiss();
            this.activity.startActivity(new Intent(this.activity, MessageViewActivity.class));
        } catch (JSONException e) {
            String hint = "Username does not exists...";
            this.jsonResponse = new JSONObject();
            SpannableString str = new SpannableString(hint);
            str.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0000")), 0, hint.length(), 0);
            this.username_hint.setText(str);
        }
    }
}
