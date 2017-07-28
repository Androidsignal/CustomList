package com.tops.xmlparsingdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class DomActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dom);

        listView = (ListView) findViewById(R.id.myList);
        arrayList = new ArrayList<>();

        getDomData obj = new getDomData();
        obj.execute();
    }

    class getDomData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(DomActivity.this);
            progressDialog.setTitle("Wait");
            progressDialog.setMessage("Data is Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://www.xmlfiles.com/examples/cd_catalog.xml");
//                http://api.androidhive.info/pizza/?format=xml
                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
                Document document = documentBuilder.parse(url.openStream());
                NodeList nodeList = document.getElementsByTagName("CD");
                for (int i=0; i<nodeList.getLength(); i++) {
                    Element element = (Element) nodeList.item(i);
                    arrayList.add(element.getElementsByTagName("TITLE").item(0).getTextContent() + " : " + element.getElementsByTagName("YEAR").item(0).getTextContent());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            ArrayAdapter arrayAdapter = new ArrayAdapter(DomActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
        }
    }
}
