package novoda.com.sandbox;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayAdapter<String> adapter;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resetSignInStateOnRotation(savedInstanceState);

        signInButton = (Button) findViewById(R.id.main_activity_sign_in_button);
        signInButton.setOnClickListener(onSignInOutClicked);

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1) {
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                view.setOnClickListener(onItemClicked);
                view.setTag(position);
                return view;
            }
        };

        ListView listView = (ListView) findViewById(R.id.main_activity_random_numbers_passwords);
        listView.setAdapter(adapter);
    }

    private void resetSignInStateOnRotation(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            Application.setSignedOut();
        }
    }

    private final View.OnClickListener onSignInOutClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (Application.isSignedIn()) {
                Application.setSignedOut();
                refreshUi();
            } else {
                startActivity(SignInActivity.createIntent());
            }
        }
    };

    private final View.OnClickListener onItemClicked = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String packageName = adapter.getItem((Integer) v.getTag());
            startActivity(DetailsActivity.createIntent(packageName));
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        refreshUi();
    }

    private void refreshUi() {
        updateSignInState();
        showData();
    }

    private void updateSignInState() {
        signInButton.setText(Application.isSignedIn() ? "Sign out" : "Sign in");
    }

    private void showData() {
        adapter.clear();
        List<ApplicationInfo> installedApplications = getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA);
        if (Application.isSignedIn()) {
            for (ApplicationInfo application : installedApplications) {
                adapter.add(application.packageName);
            }
        } else {
            adapter.add(installedApplications.get(0).packageName);
        }
    }
}
