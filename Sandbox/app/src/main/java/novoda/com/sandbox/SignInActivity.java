package novoda.com.sandbox;

import android.app.*;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class SignInActivity extends Activity {

    private static final String ACTION = BuildConfig.APPLICATION_ID + ".SIGN_IN";
    private static final int MINIMUM_INPUT_LENGTH = 4;

    private EditText userNameEditText;
    private EditText passwordEditText;
    private View submitButton;
    private AlertDialog invalidCredentialsDialog;

    public static Intent createIntent() {
        return new Intent(ACTION);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        userNameEditText = (EditText) findViewById(R.id.sign_in_activity_username_field);
        passwordEditText = (EditText) findViewById(R.id.sign_in_activity_password_field);

        userNameEditText.addTextChangedListener(signInEnablingSubmitTextWatcher);
        passwordEditText.addTextChangedListener(signInEnablingSubmitTextWatcher);

        invalidCredentialsDialog = new AlertDialog.Builder(SignInActivity.this).setMessage("Oops something went wrong, is your username and password more than 4 characters?")
                .setNeutralButton("ok", null)
                .create();

        submitButton = findViewById(R.id.sign_in_activity_submit_button);
        submitButton.setEnabled(false);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (credentialsAreInvalid()) {
                    invalidCredentialsDialog.show();
                } else {
                    Application.setSignedIn();
                    finish();
                }
            }
        });

    }

    private boolean credentialsAreInvalid() {
        return userNameEditText.getText().toString().length() < MINIMUM_INPUT_LENGTH
                || passwordEditText.getText().toString().length() < MINIMUM_INPUT_LENGTH;
    }

    private TextWatcher signInEnablingSubmitTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // do nothing
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            submitButton.setEnabled(userNameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0);
        }

        @Override
        public void afterTextChanged(Editable s) {
            // do nothing
        }
    };
}
