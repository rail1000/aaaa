package com.example.myapplication;

import android.app.ListActivity;
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
import java.util.ArrayList;
import java.util.List;

public class RateListActivity extends ListActivity implements Runnable{
    String data[] = {"a","b","c"};
    Handler handler;
    private static final String TAG = "now";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_rate_list);
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
        Bundle bundle = new Bundle();

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
            reList.add(td1+"==>"+td2);
        }

        Message msg=handler.obtainMessage(7);
        msg.obj=bundle;

        //send
        handler.sendMessage(msg);

    }
}
