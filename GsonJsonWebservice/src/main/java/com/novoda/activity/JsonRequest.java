package com.novoda.activity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.novoda.R;
import com.novoda.model.Result;
import com.novoda.model.SearchResponse;

public class JsonRequest extends Activity {
	
	private static final String SEARCH = "Droidcon";
	String url = "http://search.twitter.com/search.json?q=" + SEARCH;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((TextView)findViewById(R.id.search)).setText("Searching for: " + SEARCH);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	Toast.makeText(this, "Querying droidcon on Twitter", Toast.LENGTH_SHORT).show();
    	
        Reader reader = new InputStreamReader(retrieveStream(url));
        SearchResponse response = new Gson().fromJson(reader, SearchResponse.class);
        
        List<String> searches = new ArrayList<String>();
        Iterator<Result> i = response.results.iterator();
        while(i.hasNext()){
        	Result res = (Result )i.next();
        	searches.add(res.text);
        }
        
        ListView v = (ListView)findViewById(R.id.list);
        v.setAdapter(new ArrayAdapter<String>(this,
        		android.R.layout.simple_list_item_1, searches.toArray(new String[searches.size()])));
    }
    
    private InputStream retrieveStream(String url) {
    	
    	DefaultHttpClient client = new DefaultHttpClient(); 
        HttpGet getRequest = new HttpGet(url);
          
        try {
           
           HttpResponse getResponse = client.execute(getRequest);
           final int statusCode = getResponse.getStatusLine().getStatusCode();
           
           if (statusCode != HttpStatus.SC_OK) { 
              Log.w(getClass().getSimpleName(), "Error " + statusCode + " for URL " + url); 
              return null;
           }

           HttpEntity getResponseEntity = getResponse.getEntity();
           return getResponseEntity.getContent();
           
        } 
        catch (IOException e) {
           getRequest.abort();
           Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
        }
        return null;
     }
}