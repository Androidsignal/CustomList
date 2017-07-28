package com.tops.xmlparsingdemo;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class SaxActivity extends AppCompatActivity {

    ListView listView;
    ArrayList<String> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sax);

        listView = (ListView) findViewById(R.id.myList);
        arrayList = new ArrayList<>();

        GetSaxData getSaxData = new GetSaxData();
        getSaxData.execute();
    }

    class GetSaxData extends AsyncTask<Void, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(SaxActivity.this);
            progressDialog.setMessage("Data is Loading...");
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL url = new URL("http://www.xmlfiles.com/examples/cd_catalog.xml");
                SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
                SAXParser saxParser = saxParserFactory.newSAXParser();
                DefaultHandler defaultHandler = new DefaultHandler() {

                    boolean isArtist, isPrice;
                    String artist, price;

                    @Override
                    public void startElement
                            (String uri, String localName, String qName, Attributes attributes) throws SAXException {
                        super.startElement(uri, localName, qName, attributes);

                        if (localName.equals("ARTIST")) {
                            isArtist = true;
                        } else if (localName.equals("PRICE")) {
                            isPrice = true;
                        }
                    }

                    @Override
                    public void characters(char[] ch, int start, int length) throws SAXException {
                        super.characters(ch, start, length);

                        if (isArtist) {
                            artist = new String(ch, start, length);
                        } else if (isPrice) {
                            price = new String(ch, start, length);
                            arrayList.add(artist + " - " + price);
                        }
                    }

                    @Override
                    public void endElement(String uri, String localName, String qName) throws SAXException {
                        super.endElement(uri, localName, qName);

                        if (localName.equals("ARTIST")) {
                            isArtist = false;
                        } else if (localName.equals("PRICE")) {
                            isPrice = false;
                        }
                    }
                };
                saxParser.parse(url.openStream(), defaultHandler);
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
            ArrayAdapter arrayAdapter = new ArrayAdapter(SaxActivity.this, android.R.layout.simple_list_item_1, arrayList);
            listView.setAdapter(arrayAdapter);
        }
    }
}
