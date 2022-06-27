package com.ankit.rajmagram;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog.Builder;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    DBHandler db = new DBHandler(this);
    Button login;
    EditText password_input;

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
                return null;
            }
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(String response) {
            String str = "data";
            if (response != null) {
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d("Tag", json.toString());
                    int status = json.getInt("status");
                    LoginActivity.this.login.setClickable(true);
                    if (status == 200) {
                        String username = json.getJSONObject(str).getString("username");
                        String first_name = json.getJSONObject(str).getString("first_name");
                        str = json.getJSONObject(str).getString("last_name");
                        String password = LoginActivity.this.password_input.getText().toString();
                        Globals.current_user = username;
                        Globals.password = password;
                        LoginActivity.this.db.save_login_details(first_name, str, username, password);
                        LoginActivity.this.startActivity(new Intent(LoginActivity.this, MainActivity.class));
                        return;
                    }
                    Builder ab = new Builder(LoginActivity.this);
                    ab.setTitle("ERROR");
                    ab.setMessage("Invalid USERNAME or PASSWORD").setPositiveButton("Ok", null).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (this.db.get_login_details().size() == 0) {
            setContentView(R.layout.activity_login);
            EditText username_input = (EditText) findViewById(R.id.username);
            this.password_input = (EditText) findViewById(R.id.password);
            Button button = (Button) findViewById(R.id.btn_login);
            this.login = button;
            button.setOnClickListener(new -$$Lambda$LoginActivity$M-74wgu5og9uR2wm7W4kQg5XUE8(this, username_input));
            findViewById(R.id.btn_signup_activity).setOnClickListener(new -$$Lambda$LoginActivity$VfOxnWCB0qvaC4MgO6H3ffpeHcs(this));
            return;
        }
        ArrayList login_details = this.db.get_login_details();
        Globals.current_user = (String) login_details.get(2);
        Globals.password = (String) login_details.get(3);
        startActivity(new Intent(this, MainActivity.class));
    }

    public /* synthetic */ void lambda$onCreate$0$LoginActivity(EditText username_input, View v) {
        PrintStream printStream = System.out;
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
        stringBuilder.append(username_input.getText().toString());
        String str = "";
        printStream.println(stringBuilder.toString() != str);
        this.login.setClickable(false);
        if (username_input.getText().toString().matches(str) && this.password_input.getText().toString().matches(str)) {
            Toast.makeText(this, "Fields can not be empty", 0).show();
        } else {
            new retrievedata().execute(new String[]{Globals.login_url(username_input.getText().toString(), this.password_input.getText().toString())});
        }
    }

    public /* synthetic */ void lambda$onCreate$1$LoginActivity(View v) {
        startActivity(new Intent(this, SignUpActivity.class));
    }
}
