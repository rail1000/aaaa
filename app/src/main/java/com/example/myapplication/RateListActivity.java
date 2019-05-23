package com.example.myapplication;

import android.app.ListActivity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[] = {"a","b","c"};
    Handler handler;
    private static final String TAG = "now";
    private String logDate = "";
    private final String DATE_SP_KEY = "lastRAteDateStr";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
        SharedPreferences sp = getSharedPreferences("myrate",Context.MODE_PRIVATE);
        logDate = sp.getString(DATE_SP_KEY,"");




        List<String> list1 = new ArrayList<>();
        for(int i =1;i<100;i++){

            Thread t= new Thread(this);
            t.start();
        }
        ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        setListAdapter(adapter);

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 7){

                    List<String> list2 = (List<String>)msg.obj;
                    ListAdapter adapter1 = new ArrayAdapter<String>(RateListActivity.this,android.R.layout.simple_list_item_1,list2);
                }
            }
        };


    }
    @Override
    public void run(){

        List<String> reList = new ArrayList<String>();
        String curDateStr = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());

        if(curDateStr.equals(logDate)){
            //相等不获取数据
            RateManager manager = new RateManager(this);
            for(RateItem item:manager.listAll()){

                reList.add(item.getCurName()+ "--->"+ item.getCurRate());


            }

        }
        else{
//不等获取数据
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

            List<RateItem> rateList = new ArrayList<RateItem>();

            for(int i=0;i<trs.size();i+=5){
                Element td1=tds.get(i);
                Log.i(TAG,td1.text());
                Element td2=tds.get(i+4);
                Log.i(TAG,td2.text());
                reList.add(td1+"==>"+td2);
                rateList.add(new RateItem(td1.text(),td2.text()));
            }

            //写入数据库
            RateManager manager = new RateManager(this);
            manager.addAll(rateList);

            //记里跟新日期
            SharedPreferences sp = getSharedPreferences("myrate",Context.MODE_PRIVATE);
            SharedPreferences.Editor edit = sp.edit();
            edit.putString(DATE_SP_KEY,curDateStr);
            edit.commit();


        }

        Bundle bundle = new Bundle();



        Message msg=handler.obtainMessage(7);
        msg.obj=bundle;

        //send
        handler.sendMessage(msg);

    }
}
