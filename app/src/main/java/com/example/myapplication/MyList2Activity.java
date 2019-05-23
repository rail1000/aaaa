package com.example.myapplication;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyList2Activity extends ListActivity implements Runnable,AdapterView.OnItemClickListener,AdapterView.OnItemLongClickListener{
    private static final String TAG = "now";
    Handler handler;
    ArrayList<HashMap<String,String>> listItems;//save words photo etc
    SimpleAdapter listItemAdapter;// adapter

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initListView();
        //MyAdapter myAdapter = new MyAdapter(this,R.layout.list_item,listItems);
        this.setListAdapter(listItemAdapter);


        Thread t= new Thread((Runnable) this);
        t.start();

        //ListAdapter adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        //setListAdapter(adapter);

        handler =new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if(msg.what == 7){

                    List<HashMap<String,String>> listItems = (List<HashMap<String, String>>)msg.obj;
                    listItemAdapter  = new SimpleAdapter(MyList2Activity.this,listItems,
                            R.layout.list_item,
                            new String[]{"ItemTitle","ItemDetail"},
                            new int[]{R.id.itemTitle,R.id.itemDetail});
                    setListAdapter(listItemAdapter);
                }
            }
        };
        //
        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
              HashMap<String,String> map =(HashMap<String,String>) getListView().getItemAtPosition(position);
                String titleStr = map.get("ItemTitle");
              String detailStr = map.get("ItemDetail");
                TextView title = (TextView) view.findViewById(R.id.itemTitle);
                String title2 = String.valueOf(title.getText());

                //打开新的页面
            /*    Intent rateCalc = new Intent(this,RateCalcActivity.class);
                rateCalc.putExtra("title",titleStr);
                rateCalc.putExtra("rate",Float.parseFloat(detailStr));
                startActivity(rateCalc);

                */
            }
        });
        getListView().setOnItemLongClickListener(this);

    }

    public void initListView(){

        listItems = new ArrayList<HashMap<String, String>>();
        for(int i = 0;i<10;i++){
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle","Rate:"+i);
            map.put("ItemDetail","detail:"+i);
            listItems.add(map);
        }
        //生成适配器的item和动态数组元素
        listItemAdapter = new SimpleAdapter(this,listItems,R.id.list,
                new String[]{"ItemTitle","ItemDetail"},
                new int[]{R.id.itemTitle,R.id.itemDetail});

    }

    @Override
    public void run(){

        List<HashMap<String,String>> reList = new ArrayList<HashMap<String,String>>();
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
           String str1 = td1.text();
           String val = td2.text();
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("ItemTitle",str1);
            map.put("ItemDetail",val);
            //listItems.add(map);
            reList.add(map);
        }

        Message msg=handler.obtainMessage(7);
        msg.obj=bundle;

        //send
        handler.sendMessage(msg);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //listItems.remove(position);
        //listItems.notifyDataSetChanged();
        //对话框
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("请确认").setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listItems.remove(position);
                //listItems.notifyDataSetChanged();
            }
        })
                .setNegativeButton("否",null);
        builder.create().show();
        return true;
    }
}

