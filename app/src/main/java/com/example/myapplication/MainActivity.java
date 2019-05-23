package com.example.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

//import android.support.annotation.Nullable;

public class MainActivity extends AppCompatActivity implements Runnable{
    private static final String TAG = "now";
    double rate_a = 0.1;
     double rate_b = 0.2;
     double rate_c = 0.3;
     double rate_d = 0.4;
     String upDate = "";
    EditText rmb;
    TextView show;
    Handler handl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rmb = (EditText)findViewById(R.id.rmb);
        show = (TextView)findViewById(R.id.show);

        SharedPreferences shp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
       SharedPreferences sp= PreferenceManager.getDefaultSharedPreferences(this);
        rate_a=  shp.getFloat("rate_a",0.0f);
        rate_b= shp.getFloat("rate_b",0.0f);
        rate_c=  shp.getFloat("rate_c",0.0f);
        upDate = shp.getString("upDate","");

        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

        final String todayStr = sdf.format(today);

        if(!todayStr.equals(upDate)){

            //开启子线程
            Thread thread =new Thread(this);
            thread.start();
        }

        handl = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.what==5){
                Bundle temp=(Bundle) msg.obj;
                    rate_a= temp.getFloat("rate_a",0.0f);
                    rate_b= temp.getFloat("rate_b",0.0f);
                    rate_c= temp.getFloat("rate_c",0.0f);

                    //保存更新的日期
                    SharedPreferences sharedPreferences = getSharedPreferences("myrate",Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("upDate",todayStr);
                    editor.apply();
                }
                super.handleMessage(msg);
            }
        };

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.rate,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.menu_set){

            Intent config = new Intent(this, ConfigActivity.class);
            config.putExtra("rate_a",rate_a);
            config.putExtra("rate_b",rate_b);
            config.putExtra("rate_c",rate_c);
            config.putExtra("rate_d",rate_d);


            //startActivity(config);
            startActivityForResult(config,1);
        }
        else if(item.getItemId() == R.id.list){
            Intent list = new Intent(this,MyList2Activity.class);
            startActivity(list);
            /*

            RateItem item1 = new RateItem("aaa","123");
            RateManager manager = new RateManager(this);
            manager.add(item1);
            manager.add(new RateItem("bbb","456"));

            //查询所有数据
            List<RateItem> testList = manager.listAll();
            for(RateItem i:testList){
                Log.i("tag","onOptionsItemSelected:[id = "+i.getId());


            }
            */
        }
        return super.onOptionsItemSelected(item);
    }

    public void openconfig(){

        Intent config = new Intent(this, ConfigActivity.class);
        config.putExtra("rate_a",rate_a);
        config.putExtra("rate_b",rate_b);
        config.putExtra("rate_c",rate_c);
        config.putExtra("rate_d",rate_d);


        //startActivity(config);
        startActivityForResult(config,1);
    }
    public void openone(View btn) {
   openconfig();



    }

    public void onclick(View btn) {

    String str = rmb.getText().toString();
    double money = Integer.parseInt(str);

    if(btn.getId() == R.id.a){

show.setText(String.format("%.2f",money*rate_a));

    }
    else if(btn.getId() == R.id.b){
        show.setText(String.format("%.2f",money*rate_b));

    }
    else if(btn.getId() == R.id.c){
        show.setText(String.format("%.2f",money*rate_c));

    }
    else if(btn.getId() == R.id.open_list){

//打开列表
        Intent list = new Intent(this, ConfigActivity.class);

        startActivity(list);
    }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        if(resultCode==2&&requestCode==1){

            Bundle bunl=data.getExtras();
            rate_a=bunl.getDouble("rate_a",0.0);
            rate_b=bunl.getDouble("rate_b",0.0);
            rate_c=bunl.getDouble("rate_c",0.0);

            SharedPreferences shp=getSharedPreferences("myrate", Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor=shp.edit();
            editor.putFloat("rate_a",(float) rate_a);
            editor.putFloat("rate_b",(float) rate_b);
            editor.putFloat("rate_c",(float) rate_c);
            editor.commit();

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void run() {



        URL url = null;
        Bundle bundle = new Bundle();
        try {
            url = new URL("http://www.usd-cny.com/icbc.htm");
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            HttpURLConnection http=(HttpURLConnection) url.openConnection();
            InputStream in=http.getInputStream();

            String html = inputStream2String(in);
            Log.i(TAG,html);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        //新的ArrayList类型
        List<HashMap<String,String>> retList = new ArrayList<HashMap<String,String>>();

        Document doc = null;
        try {
            doc = Jsoup.connect("http://www.usd-cny.com/icbc.htm").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.i(TAG,doc.title());
        Elements trs = doc.getElementsByTag("tr");


        Element tr6 = trs.get(5);
        Elements tds = tr6.getElementsByTag("td");
        for(int i=0;i<trs.size();i+=5){
            Element td1=tds.get(i);
            Log.i(TAG,td1.text());
            Element td2=tds.get(i+4);
            Log.i(TAG,td2.text());
        /*
            if("美元".equals(td1.text())){
                bundle.putFloat("rate_a",100f/Float.parseFloat(td1.text()));
            }
            else if("欧元".equals(td1.text())){
                bundle.putFloat("rate_b",100f/Float.parseFloat(td1.text()));
            }
            else if("韩国元".equals(td1.text())){
                bundle.putFloat("rate_c",100f/Float.parseFloat(td1.text()));
            }

        */
        String str1 = td1.text();
        String val = td2.text();
        HashMap<String,String> map= new HashMap<String,String>();
        map.put("ItemTitle",str1);
        map.put("ItemDetail",val);
        retList.add(map);
        }
        Message msg=handl.obtainMessage(5);
        msg.obj=bundle;

        //send
        handl.sendMessage(msg);


    }

    public static String inputStream2String(InputStream inputStream)throws IOException{
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "gb2312");
        for (; ; ) {
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        return out.toString();
    }

}
