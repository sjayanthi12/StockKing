package com.example.StockKing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity{
    public final static String EXTRA_MESSAGE = "com.example.StockKing.MESSAGE";
    String message = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final EditText myEditText = (EditText)findViewById(R.id.edit_message);
        myEditText.setOnClickListener(new EditText.OnClickListener(){
            public void onClick(View v){
                myEditText.setText("");
            }
        });
        final Button myButton = (Button)findViewById(R.id.my_button);
        myButton.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v) {
                LongRunningGetIO task = new LongRunningGetIO();
                //	task.applicationContext = Start.this;
                task.execute();
    }
        });
    }

    private class LongRunningGetIO extends AsyncTask <Void, Void, String> {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n>0) {
                byte[] b = new byte[4096];
                n =  in.read(b);
                if (n>0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }
        @Override
        protected String doInBackground(Void... params) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet("https://www.google.com/finance/info?infotype=infoquoteall&q="+ ((EditText)findViewById(R.id.edit_message)).getText().toString());
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                text = parseJson(text, "high data");
            } catch (Exception e) {
                return e.getLocalizedMessage();
            }
            return text;
        }
        protected void onPostExecute(String results) {
            if (results!=null) {
                EditText et = (EditText)findViewById(R.id.edit_message);
                et.setText(results);
            }
            Button b = (Button)findViewById(R.id.my_button);
            b.setClickable(true);
        }
    }

    public String parseJson(String json, String query){
        JSONObject newJson;
        try{
            newJson = new JSONObject(json);
            //	JSONObject result = newJson.getJSONObject("finance");
            String data = newJson.getString("name") + ": hi " + newJson.getString("hi") + ": lo " + newJson.getString("lo");
            return data;
        }catch(Exception e){
            return "0.0";
        }
    }

    public void sendMessage(View view){
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        //message = editText.getText().toString();
        //new LongRunningGetIO().execute();

        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

}
