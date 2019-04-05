package slasha.lanmu.business.login;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import slasha.lanmu.R;
import slasha.lanmu.SameStyleActivity;
import slasha.lanmu.business.main.MainActivity;

public class LoginActivity extends SameStyleActivity implements LoginContract.LoginView {

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private LoginContract.LoginPresenter mLoginPresenter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set up the login form.
        mEmailView = findViewById(R.id.edt_email);

        mPasswordView = findViewById(R.id.edt_password);
        mPasswordView.setOnEditorActionListener((textView, id, keyEvent) -> {
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                String email = mEmailView.getText().toString();
                String password = mPasswordView.getText().toString();
                if (checkInputCorrection(password, email)) {
                    showProgressDialog();
                    myPresenter().performLogin(password, email);
                }
                return true;
            }
            return false;
        });

        populateAutoComplete();

        findViewById(R.id.btn_email_sign_in)
                .setOnClickListener(view -> {
                    String email = mEmailView.getText().toString();
                    String password = mPasswordView.getText().toString();
                    if (checkInputCorrection(password, email)) {
                        showProgressDialog();
                        myPresenter().performLogin(password, email);
                    }
                });

        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setDisplayHomeAsUpEnabled(false);
            supportActionBar.setDisplayShowHomeEnabled(false);
        }

        setTitle(R.string.login_title);
    }

    private void populateAutoComplete() {
        // todo: 自动填充
        mEmailView.setText("foo@example.com");
        mPasswordView.setText("hello");
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_login;
    }

    private boolean checkInputCorrection(String password, String email) {
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }

        return !cancel;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    @Override
    public void showLoginResult(boolean success) {
        if (success) {
            finish();
            jumpToMainPage();
        } else {
            mPasswordView.setError(getString(R.string.error_incorrect_password));
            mPasswordView.requestFocus();
        }
        hideProgressDialog();
    }

    @Override
    public LoginContract.LoginPresenter myPresenter() {
        if (mLoginPresenter == null) {
            mLoginPresenter = new LoginPresenterImpl(this, new LoginModelImpl());
        }
        return mLoginPresenter;
    }

    @Override
    public void showLoadingIndicator() {
        showProgressDialog();
    }

    @Override
    public void hideLoadingIndicator() {
        hideProgressDialog();
    }

    private void jumpToMainPage() {
        startActivity(
                MainActivity.newIntent(this)
        );
    }

}

