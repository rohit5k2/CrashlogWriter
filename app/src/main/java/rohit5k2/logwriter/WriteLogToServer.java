package rohit5k2.logwriter;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WriteLogToServer extends AsyncTask<String, Void, Integer> {

    protected void onPreExecute(){}

    protected Integer doInBackground(String... arg0) {

        try {
            URL url = new URL("http://10.0.2.2/logwriter/api/write"); //Enter URL here
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestMethod("POST"); // here you are telling that it is a POST request, which can be changed into "PUT", "GET", "DELETE" etc.
            httpURLConnection.setRequestProperty("Content-Type", "application/json"); // here you are setting the `Content-Type` for the data you are sending which is `application/json

            JSONObject data = new JSONObject();
            data.put("deviceId", arg0[0]);
            data.put("timestamp", arg0[1]);
            data.put("stacktrace", arg0[2]);

            DataOutputStream wr = new DataOutputStream(httpURLConnection.getOutputStream());
            wr.writeBytes(data.toString());
            wr.flush();
            wr.close();

            return httpURLConnection.getResponseCode();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    @Override
    protected void onPostExecute(Integer result) {

    }
}