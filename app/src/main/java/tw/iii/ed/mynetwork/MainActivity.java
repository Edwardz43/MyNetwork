package tw.iii.ed.mynetwork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.Okio;

public class MainActivity extends AppCompatActivity {
    private ImageView img, img2;
    private Bitmap bitmap, bitmap2;
    private UIHandler handler;
    private GetExample example;
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        img2 = (ImageView) findViewById(R.id.img2);
        textView = (TextView)findViewById(R.id.tv1);
        handler = new UIHandler();

    }

    public void test1(View view){
        final long startTime = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                URL url = null;
                try {
                    url = new URL("http://www.iii.org.tw/");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.connect();
                    InputStream in = conn.getInputStream();
                    BufferedReader br = new BufferedReader(new InputStreamReader(in));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine())!= null){
                        sb.append(line + "\n");
                    }
                    handler.sendEmptyMessage(1);
                    String report = "1 :"+(System.currentTimeMillis() - startTime);
                    Log.i("brad", report);
                } catch (Exception e) {
                    Log.i("brad", e.toString());
                }
            }
        }.start();

    }
    public void test2(View view){
        final long startTime = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://www.fakingnews.firstpost.com/wp-content/uploads/2015/12/kim-jaya.jpg");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    handler.sendEmptyMessage(2);
                    String report = "2 :"+(System.currentTimeMillis() - startTime);
                    Log.i("brad", report);
                }catch (Exception e){
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    // use OkHttp method
    public void test3(View view) throws IOException{
        final long startTime = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                example = new GetExample();
                Response response = null;
                try {
                    response = example.run("http://www.iii.org.tw/");
                    handler.sendEmptyMessage(3);
                    String report = "3 :"+(System.currentTimeMillis() - startTime);
                    Log.i("brad", report);
                } catch (IOException e) {
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    public void  test4(View view){
        final long startTime = System.currentTimeMillis();
        new Thread(){
            @Override
            public void run() {
                String url = "http://www.fakingnews.firstpost.com/wp-content/uploads/2015/12/kim-jaya.jpg";
                example = new GetExample();
                Response response = null;
                try {
                    response = example.run(url);
                    byte[] bytes = response.body().bytes();
                    bitmap2 = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    handler.sendEmptyMessage(4);
                    String report = "4 :"+(System.currentTimeMillis() - startTime);
                    Log.i("brad", report);
                } catch (IOException e) {
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    public void test5(View view){
        OkHttpClient client = new OkHttpClient();
        String url = "";
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("brad", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Okio.sink(new FileOutputStream(new File("")));

            }
        });

    }

    private  class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //Log.i("brad", ""+msg.what);
            if (msg.what == 0){
                img.setImageBitmap(bitmap);
            }else if (msg.what == 1){
                img2.setImageBitmap(bitmap2);
            }

        }
    }

    // define class with OkHttp
    private class GetExample {
        OkHttpClient client = new OkHttpClient();
        //downloads a URL and return its self as response object
        private Response run(String url) throws IOException {
            Request request = new Request.Builder().url(url).build();
            return client.newCall(request).execute();
        }
    }
}
