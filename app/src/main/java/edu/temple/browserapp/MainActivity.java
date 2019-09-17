package edu.temple.browserapp;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView urlEditText;
    URL url;
    Handler responseHandler;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        urlEditText = findViewById(R.id.urlEditText);
        webView = findViewById(R.id.webView);

        findViewById(R.id.gobutton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new Thread() {
                    public void run() {
                        try {
                            url = new URL(urlEditText.getText().toString());
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(
                                            url.openStream()));

                            StringBuilder sb = new StringBuilder();
                            String tmpString;

                            while ((tmpString = reader.readLine()) != null) {
                                sb.append(tmpString);
                            }

                            String response = sb.toString();
                            Message msg = Message.obtain();
                            msg.obj = response;
                            responseHandler.sendMessage(msg);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }.start();



                 responseHandler = new Handler(new Handler.Callback() {
                    @Override
                    public boolean handleMessage(Message msg) {
                        webView.loadData((String) msg.obj, "text/html", "UTF-8");
                        return false;
                    }
                });
            }
        });

        /*public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.);
        }*/
    }
}
