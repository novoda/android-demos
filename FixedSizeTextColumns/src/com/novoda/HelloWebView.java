package com.novoda;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

public class HelloWebView extends Activity {

	private static final int DIALOG_CHOOSE_VIEW_ID = 0;
	private WebView webView;
	private int fontSize;

	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.webview_main);
	    webView = (WebView) findViewById(R.id.webview);
	    webView.loadUrl("file:///android_asset/two_columns.html");
	    fontSize = webView.getSettings().getDefaultFontSize();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.webview_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.zoom_in:
	    	fontSizePlus();
	        return true;
	    case R.id.choose_view:
	    	showDialog(DIALOG_CHOOSE_VIEW_ID);
	    	return true;
	    case R.id.zoom_out:
	    	fontSizeMinus();
	        return true;
	    default:
	        return super.onOptionsItemSelected(item);
	    }
	}
	
	private void fontSizePlus() {
	    fontSize++;
	    webView.getSettings().setDefaultFontSize(fontSize);
	}

	private void fontSizeMinus() {
	    fontSize--;
	    webView.getSettings().setDefaultFontSize(fontSize);
	}
	
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
			case DIALOG_CHOOSE_VIEW_ID:
				return getChooseViewDialog();
		}
		return null;
	}
	
	private Dialog getChooseViewDialog() {
		final String[] fileNames = {"Two columns", "Three columns", "Two columns and photo"};
		final String[] filePaths = {"file:///android_asset/two_columns.html",
									"file:///android_asset/three_columns.html",
									"file:///android_asset/two_columns_and_photo.html"};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Choose a View");
		builder.setItems(fileNames, new DialogInterface.OnClickListener() {
		    public void onClick(DialogInterface dialog, int item) {
		    	webView.loadUrl(filePaths[item]);
		    }
		});
		
		return builder.create();
	}
}