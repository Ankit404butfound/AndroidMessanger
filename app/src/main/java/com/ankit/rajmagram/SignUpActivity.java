package com.ankit.rajmagram;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import org.json.JSONException;
import org.json.JSONObject;

public class SignUpActivity extends AppCompatActivity {
    Button btn_sign_up;
    DBHandler db;
    EditText firstname;
    String fname;
    EditText lastname;
    String lname;
    String uname;
    EditText username;

    class sign_up extends AsyncTask<String, String, String> {
        sign_up() {
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
                return null;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d("Tag", json.toString());
                    int status = json.getInt("status");
                    SignUpActivity.this.btn_sign_up.setClickable(true);
                    if (status == 200) {
                        String password = json.getJSONObject("user_info").getString("password");
                        Globals.current_user = SignUpActivity.this.uname;
                        Globals.password = password;
                        SignUpActivity.this.db.save_login_details(SignUpActivity.this.fname, SignUpActivity.this.lname, SignUpActivity.this.uname, password);
                        SignUpActivity.this.startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                        return;
                    }
                    Builder ab = new Builder(SignUpActivity.this);
                    ab.setTitle("ERROR");
                    ab.setMessage("USERNAME TAKEN").setPositiveButton("Ok", null).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        this.db = new DBHandler(this);
        this.firstname = (EditText) findViewById(R.id.firstname);
        this.lastname = (EditText) findViewById(R.id.lastname);
        this.username = (EditText) findViewById(R.id.username);
        Button button = (Button) findViewById(R.id.btn_signup);
        this.btn_sign_up = button;
        button.setOnClickListener(new -$$Lambda$SignUpActivity$NYHJpY0i3K8oxsi3hqMznT2uhGk(this));
        if (VERSION.SDK_INT >= 21) {
            getWindow().addFlags(Integer.MIN_VALUE);
        }
        findViewById(R.id.btn_login_activity).setOnClickListener(new -$$Lambda$SignUpActivity$WR6jm-ifK306aOiFa9S-T6XtR_E(this));
    }

    public /* synthetic */ void lambda$onCreate$0$SignUpActivity(View v) {
        this.uname = this.username.getText().toString();
        this.fname = this.firstname.getText().toString();
        this.lname = this.lastname.getText().toString();
        String str = "";
        if (!this.uname.matches(str) && !this.lname.matches(str) && !this.fname.matches(str)) {
            new sign_up().execute(new String[]{Globals.sign_up_url(this.fname, this.lname, this.username.getText().toString())});
        }
    }

    public /* synthetic */ void lambda$onCreate$1$SignUpActivity(View v) {
        finish();
    }
}
