package com.tops.xmlparsingdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

public class PullActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull);

        listView = (ListView) findViewById(R.id.myList);
        arrayList = new ArrayList<>();

        XmlPullParser xmlPullParser = getResources().getXml(R.xml.my_data);

        try {
            while (xmlPullParser.next() != XmlPullParser.END_DOCUMENT) {
                String name = "";
                for (int i=0; i<xmlPullParser.getAttributeCount(); i++) {

                    if (xmlPullParser.getAttributeName(i).equals("name")) {
                        name = xmlPullParser.getAttributeValue(i);
                    } else {
                        arrayList.add(name + " = " + xmlPullParser.getAttributeValue(i));
                    }
                }
            }
            ArrayAdapter arrayAdapter = new ArrayAdapter(PullActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
