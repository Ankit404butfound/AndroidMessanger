package com.ankit.rajmagram;

import android.os.AsyncTask;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

class NetworkManager extends AsyncTask<String, String, String> {
    NetworkManager() {
    }

    /* Access modifiers changed, original: protected|varargs */
    public String doInBackground(String... the_url) {
        String str = "true";
        String str2 = "false";
        URL url = null;
        try {
            url = new URL(the_url[0]);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            if (conn.getResponseCode() == 200) {
                try {
                    BufferedInputStream bufferedInputStream = new BufferedInputStream(conn.getInputStream());
                    conn.disconnect();
                    Globals.RESULT_FROM_ASYNC_TASK = str;
                    return str;
                } catch (Throwable th) {
                    conn.disconnect();
                    Globals.RESULT_FROM_ASYNC_TASK = str;
                    return str;
                }
            }
            Globals.RESULT_FROM_ASYNC_TASK = str2;
            return str2;
        } catch (IOException e2) {
            e2.printStackTrace();
            Globals.RESULT_FROM_ASYNC_TASK = str2;
            return str2;
        }
    }

    /* Access modifiers changed, original: protected */
    public void onPostExecute(String result) {
        super.onPostExecute(result);
    }
}
