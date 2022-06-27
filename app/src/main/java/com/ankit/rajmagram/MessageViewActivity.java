package com.ankit.rajmagram;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.ViewTreeObserver.OnScrollChangedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageViewActivity extends AppCompatActivity {
    public static String sent_message;
    Boolean at_bottom = Boolean.valueOf(true);
    DBHandler db = new DBHandler(this);
    private OnFocusChangeListener focusListener = new OnFocusChangeListener() {
        public void onFocusChange(View v, boolean hasFocus) {
            new Thread() {
                public void run() {
                    SystemClock.sleep(300);
                    MessageViewActivity.this.scroll.fullScroll(130);
                }
            }.start();
        }
    };
    JSONObject jsonResponse = new JSONObject();
    LinearLayout message_filed;
    ScrollView scroll;
    Thread thread;

    class send_message extends AsyncTask<String, String, String> {
        send_message() {
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
            if (response != null && response != "error") {
                try {
                    JSONObject json = new JSONObject(response);
                    Log.d("Tag", json.toString());
                    if (json.getInt("status") == 200) {
                        Globals.current_order_id = MessageViewActivity.this.db.add_outgoing_message(MessageViewActivity.sent_message, Globals.current_receiver, Globals.current_receiver_first_name, Globals.current_receiver_last_name);
                        MessageViewActivity.this.scroll.fullScroll(130);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void add_message_view(String message, String type) {
        RelativeLayout ll;
        if (type == "outgoing") {
            ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.message_layout_outgoing, null);
        } else {
            ll = (RelativeLayout) getLayoutInflater().inflate(R.layout.message_layout_incoming, null);
        }
        ((TextView) ll.findViewById(R.id.msg)).setText(message);
        LayoutParams params = new LayoutParams(-2, -2);
        params.setMargins(0, 10, 0, 10);
        ll.setLayoutParams(params);
        this.message_filed.addView(ll);
        if (this.at_bottom.booleanValue()) {
            this.scroll.fullScroll(130);
        }
    }

    /* Access modifiers changed, original: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_view);
        TextView username_area = (TextView) findViewById(R.id.username_view);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(Globals.current_receiver_first_name);
        stringBuilder.append(" ");
        stringBuilder.append(Globals.current_receiver_last_name);
        username_area.setText(stringBuilder.toString());
        ScrollView scrollView = (ScrollView) findViewById(R.id.message_area);
        this.scroll = scrollView;
        scrollView.getViewTreeObserver().addOnScrollChangedListener(new OnScrollChangedListener() {
            public void onScrollChanged() {
                if (MessageViewActivity.this.scroll.getChildAt(0).getBottom() <= MessageViewActivity.this.scroll.getHeight() + MessageViewActivity.this.scroll.getScrollY()) {
                    MessageViewActivity.this.at_bottom = Boolean.valueOf(true);
                    return;
                }
                MessageViewActivity.this.at_bottom = Boolean.valueOf(false);
            }
        });
        ((Button) findViewById(R.id.back)).setOnClickListener(new -$$Lambda$MessageViewActivity$W_Uii2z2I27Z4U2JL5I3DmGJPDY(this));
        EditText message_input = (EditText) findViewById(R.id.message_input);
        message_input.setOnFocusChangeListener(this.focusListener);
        message_input.setOnClickListener(new -$$Lambda$MessageViewActivity$7FdIKBgA3SH3R6ciE_LIclc7l4o(this));
        this.message_filed = (LinearLayout) findViewById(R.id.message_field);
        ((Button) findViewById(R.id.send)).setOnClickListener(new -$$Lambda$MessageViewActivity$yKOlMOK16szG6TxcjSA1yt_Hkw8(this, message_input));
        Iterator it = this.db.get_messages(Globals.current_receiver).iterator();
        while (it.hasNext()) {
            View inflate;
            ArrayList message_details = (ArrayList) it.next();
            String message = (String) message_details.get(1);
            String sender = (String) message_details.get(2);
            if (((String) message_details.get(3)).equals(Globals.current_user)) {
                inflate = getLayoutInflater().inflate(R.layout.message_layout_incoming, null);
            } else {
                inflate = getLayoutInflater().inflate(R.layout.message_layout_outgoing, null);
            }
            RelativeLayout message_ll = (RelativeLayout) inflate;
            ((TextView) message_ll.findViewById(R.id.msg)).setText(message);
            LayoutParams params = new LayoutParams(-2, -2);
            params.setMargins(0, 10, 0, 10);
            message_ll.setLayoutParams(params);
            this.message_filed.addView(message_ll);
        }
        this.scroll.post(new Runnable() {
            public void run() {
                MessageViewActivity.this.scroll.fullScroll(130);
            }
        });
        AnonymousClass5 anonymousClass5 = new Thread() {
            public void run() {
                while (true) {
                    MessageViewActivity.this.runOnUiThread(new -$$Lambda$MessageViewActivity$5$aFVIpxN0jswLtOCfLHxd9IQyIfQ(this));
                    SystemClock.sleep(1000);
                }
            }

            public /* synthetic */ void lambda$run$0$MessageViewActivity$5() {
                if (Globals.msg_array.size() > 0) {
                    Iterator it = Globals.msg_array.iterator();
                    while (it.hasNext()) {
                        Object msg = it.next();
                        MessageViewActivity.this.add_message_view(String.valueOf(msg), "incoming");
                        Globals.msg_array.remove(msg);
                    }
                }
                if (MessageViewActivity.this.at_bottom.booleanValue()) {
                    MessageViewActivity.this.scroll.fullScroll(130);
                }
            }
        };
        this.thread = anonymousClass5;
        anonymousClass5.start();
    }

    public /* synthetic */ void lambda$onCreate$0$MessageViewActivity(View v) {
        finish();
    }

    public /* synthetic */ void lambda$onCreate$1$MessageViewActivity(View v) {
        new Thread() {
            public void run() {
                SystemClock.sleep(300);
                MessageViewActivity.this.scroll.fullScroll(130);
            }
        }.start();
    }

    public /* synthetic */ void lambda$onCreate$2$MessageViewActivity(EditText message_input, View v) {
        String obj = message_input.getText().toString();
        sent_message = obj;
        String str = "";
        if (!obj.matches(str)) {
            add_message_view(sent_message, "outgoing");
            message_input.setText(str);
            new send_message().execute(new String[]{Globals.send_msg_url(sent_message)});
        }
    }

    public void onBackPressed() {
        String str = "";
        Globals.current_receiver = str;
        Globals.current_receiver_first_name = str;
        Globals.current_receiver_last_name = str;
        this.thread.interrupt();
        finish();
    }
}
