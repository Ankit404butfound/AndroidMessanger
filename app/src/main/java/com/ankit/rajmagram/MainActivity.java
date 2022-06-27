package com.ankit.rajmagram;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    DBHandler db = new DBHandler(this);
    JSONObject jsonResponse = new JSONObject();
    public LinearLayout master_ll;
    Runnable runnable;

    /* Access modifiers changed, original: protected */
    public void onResume() {
        super.onResume();
        rearrange_layout();
    }

    private void rearrange_layout() {
        this.master_ll.removeAllViews();
        ArrayList<ArrayList<Object>> all_users = this.db.get_all_users();
        Iterator it = all_users.iterator();
        while (it.hasNext()) {
            ArrayList user = (ArrayList) it.next();
            int order_id = ((Integer) user.get(0)).intValue();
            String first_name = String.valueOf(user.get(1));
            String last_name = String.valueOf(user.get(2));
            String username = String.valueOf(user.get(3));
            String message = String.valueOf(user.get(4));
            RelativeLayout child_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.user_area, null);
            ((TextView) child_ll.findViewById(R.id.message_count)).setVisibility(4);
            child_ll.setId(order_id);
            child_ll.setOnClickListener(new -$$Lambda$MainActivity$Zjnk3y8cEhWEsf3XsDZiIaOaw6Q(this));
            View viewDivider = new View(this);
            viewDivider.setBackgroundColor(Color.parseColor("#e9edef"));
            viewDivider.setLayoutParams(new LayoutParams(-1, (int) (getResources().getDisplayMetrics().density * 1065353216)));
            TextView user_details = (TextView) child_ll.findViewById(R.id.user_details);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(first_name);
            stringBuilder.append(" ");
            stringBuilder.append(last_name);
            String boldText = stringBuilder.toString();
            ArrayList<ArrayList<Object>> all_users2 = all_users;
            String str = "\n";
            Iterator it2 = it;
            if (message.length() >= 20) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(message.substring(0, 20));
                stringBuilder.append("...");
                str = stringBuilder.toString();
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(message);
                str = stringBuilder2.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(boldText);
            stringBuilder.append(str);
            SpannableString str2 = new SpannableString(stringBuilder.toString());
            str2.setSpan(new StyleSpan(1), 0, boldText.length(), 33);
            str2.setSpan(new ForegroundColorSpan(Color.parseColor("#141b21")), 0, boldText.length(), 0);
            user_details.setText(str2);
            LayoutParams params = new LayoutParams(-2, -2);
            params.setMargins(0, 10, 0, 10);
            child_ll.setLayoutParams(params);
            this.master_ll.addView(child_ll);
            this.master_ll.addView(viewDivider);
            all_users = all_users2;
            it = it2;
        }
    }

    public /* synthetic */ void lambda$rearrange_layout$0$MainActivity(View v) {
        Globals.current_order_id = v.getId();
        ArrayList current_user_details = this.db.get_user_by_order_id(v.getId());
        Globals.current_receiver_first_name = (String) current_user_details.get(1);
        Globals.current_receiver_last_name = (String) current_user_details.get(2);
        Globals.current_receiver = (String) current_user_details.get(3);
        startActivity(new Intent(this, MessageViewActivity.class));
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.master_ll = (LinearLayout) findViewById(R.id.user_field_layout);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new -$$Lambda$MainActivity$jiv80_JA4fCPt-NtLWbNGn81q4k(this));
        ArrayList<ArrayList<Object>> all_users = this.db.get_all_users();
        Iterator it = all_users.iterator();
        while (it.hasNext()) {
            ArrayList user = (ArrayList) it.next();
            int order_id = ((Integer) user.get(0)).intValue();
            String first_name = String.valueOf(user.get(1));
            String last_name = String.valueOf(user.get(2));
            String username = String.valueOf(user.get(3));
            String message = String.valueOf(user.get(4));
            RelativeLayout child_ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.user_area, null);
            ((TextView) child_ll.findViewById(R.id.message_count)).setVisibility(4);
            child_ll.setId(order_id);
            child_ll.setOnClickListener(new -$$Lambda$MainActivity$yR-NCU8OYgs5hVeIQNkxGWgV7rc(this));
            View viewDivider = new View(this);
            viewDivider.setBackgroundColor(Color.parseColor("#e9edef"));
            viewDivider.setLayoutParams(new LayoutParams(-1, (int) (getResources().getDisplayMetrics().density * 1065353216)));
            TextView user_details = (TextView) child_ll.findViewById(R.id.user_details);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(first_name);
            FloatingActionButton fab2 = fab;
            stringBuilder.append(" ");
            stringBuilder.append(last_name);
            fab = stringBuilder.toString();
            ArrayList<ArrayList<Object>> all_users2 = all_users;
            String str = "\n";
            Iterator it2 = it;
            if (message.length() >= 20) {
                stringBuilder = new StringBuilder();
                stringBuilder.append(str);
                stringBuilder.append(message.substring(0, 20));
                stringBuilder.append("...");
                str = stringBuilder.toString();
            } else {
                StringBuilder stringBuilder2 = new StringBuilder();
                stringBuilder2.append(str);
                stringBuilder2.append(message);
                str = stringBuilder2.toString();
            }
            stringBuilder = new StringBuilder();
            stringBuilder.append(fab);
            stringBuilder.append(str);
            SpannableString str2 = new SpannableString(stringBuilder.toString());
            str2.setSpan(new StyleSpan(1), 0, fab.length(), 33);
            str2.setSpan(new ForegroundColorSpan(Color.parseColor("#141b21")), 0, fab.length(), 0);
            user_details.setText(str2);
            LayoutParams params = new LayoutParams(-2, -2);
            params.setMargins(0, 10, 0, 10);
            child_ll.setLayoutParams(params);
            this.master_ll.addView(child_ll);
            this.master_ll.addView(viewDivider);
            fab = fab2;
            all_users = all_users2;
            it = it2;
        }
        new Thread() {
            public void run() {
                URL url = null;
                try {
                    url = new URL(Globals.new_message_url());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                while (true) {
                    System.out.println("CHECKINGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG NEW MEEEESSAGE");
                    try {
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        conn.setRequestMethod("GET");
                        if (conn.getResponseCode() == 200) {
                            try {
                                InputStream in = new BufferedInputStream(conn.getInputStream());
                                InputStream inputStream = conn.getInputStream();
                                StringBuffer buffer = new StringBuffer();
                                if (inputStream != null) {
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
                                    if (buffer.length() != 0) {
                                        JSONObject json = new JSONObject(buffer.toString());
                                        Log.d("Tag", json.toString());
                                        JSONArray Response = json.getJSONArray("data");
                                        if (json.getInt("status") == 200) {
                                            int i = 0;
                                            while (i < Response.length()) {
                                                MainActivity.this.jsonResponse = (JSONObject) Response.get(i);
                                                System.out.println(MainActivity.this.jsonResponse);
                                                InputStream in2 = in;
                                                MainActivity.this.db.add_incoming_message(MainActivity.this.jsonResponse.getString("message"), MainActivity.this.jsonResponse.getString("from_username"), "datetime('now', 'localtime')", MainActivity.this.jsonResponse.getString("first_name"), MainActivity.this.jsonResponse.getString("last_name"));
                                                MainActivity.this.runOnUiThread(new -$$Lambda$MainActivity$1$63OiaUxsZ-mu-0Il8vJUhMfTnpA(this));
                                                i++;
                                                in = in2;
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException jsonException) {
                                jsonException.printStackTrace();
                            } catch (IOException ioException) {
                                ioException.printStackTrace();
                            }
                        }
                        conn.disconnect();
                    } catch (IOException ioException2) {
                        ioException2.printStackTrace();
                    }
                }
            }

            public /* synthetic */ void lambda$run$0$MainActivity$1() {
                String str = "message";
                String str2 = "from_username";
                MainActivity.this.rearrange_layout();
                try {
                    PrintStream printStream = System.out;
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("HEEEEEEEEEEEEEEEREEEEEEEEEEE");
                    stringBuilder.append(Globals.current_receiver);
                    stringBuilder.append(MainActivity.this.jsonResponse.getString(str2));
                    printStream.println(stringBuilder.toString());
                    if (Globals.current_receiver.equals(MainActivity.this.jsonResponse.getString(str2))) {
                        Globals.msg_array.add(MainActivity.this.jsonResponse.getString(str));
                        PrintStream printStream2 = System.out;
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append("MSGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGGG");
                        stringBuilder2.append(MainActivity.this.jsonResponse.getString(str));
                        printStream2.println(stringBuilder2.toString());
                    }
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }.start();
    }

    public /* synthetic */ void lambda$onCreate$1$MainActivity(View v) {
        new CustomDialog(this).show();
    }

    public /* synthetic */ void lambda$onCreate$2$MainActivity(View v) {
        Globals.current_order_id = v.getId();
        ArrayList current_user_details = this.db.get_user_by_order_id(v.getId());
        Globals.current_receiver_first_name = (String) current_user_details.get(1);
        Globals.current_receiver_last_name = (String) current_user_details.get(2);
        Globals.current_receiver = (String) current_user_details.get(3);
        startActivity(new Intent(this, MessageViewActivity.class));
    }
}
