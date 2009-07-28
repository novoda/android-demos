package com.novoda;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ContactSelector extends Activity {
	public static final int	PICK_CONTACT	= 1;
	private Button			btnContacts;
	private TextView		txtContacts;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		btnContacts = (Button) findViewById(R.id.btn_contacts);
		txtContacts = (TextView) findViewById(R.id.txt_contacts);

		btnContacts.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(Intent.ACTION_PICK, People.CONTENT_URI);
				startActivityForResult(intent, PICK_CONTACT);
			}
		});
	}

	@Override
	public void onActivityResult(int reqCode, int resultCode, Intent data) {
		super.onActivityResult(reqCode, resultCode, data);

		switch (reqCode) {
			case (PICK_CONTACT):
				if (resultCode == Activity.RESULT_OK) {
					Uri contactData = data.getData();
					Cursor c = managedQuery(contactData, null, null, null, null);
					if (c.moveToFirst()) {
						String name = c.getString(c.getColumnIndexOrThrow(People.NAME));
						txtContacts.setText(name);
					}
				}
				break;
		}
	}
}