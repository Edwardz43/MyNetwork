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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private ImageView img;
    private Bitmap bitmap;
private UIHandler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        img = (ImageView) findViewById(R.id.img);
        handler = new UIHandler();
    }

    public void test1(View view){
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
                    Log.i("brad", sb.toString());

                } catch (Exception e) {
                    Log.i("brad", e.toString());
                }
            }
        }.start();

    }
    public void test2(View view){
        new Thread(){
            @Override
            public void run() {
                try{
                    URL url = new URL("http://www.fakingnews.firstpost.com/wp-content/uploads/2015/12/kim-jaya.jpg");
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    bitmap = BitmapFactory.decodeStream(conn.getInputStream());
                    handler.sendEmptyMessage(0);
                }catch (Exception e){
                    Log.i("brad", e.toString());
                }
            }
        }.start();
    }

    private  class UIHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            img.setImageBitmap(bitmap);
        }
    }
}
