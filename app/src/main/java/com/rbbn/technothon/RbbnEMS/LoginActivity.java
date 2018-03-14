package com.rbbn.technothon.RbbnEMS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rbbn.technothon.RbbnEMS.utils.PerformNetworkOperations;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private EditText mEMSIpview;
    private View mProgressView;
    private View mLoginFormView;
    private String launchURL;
    private String getAuthURL;
    private String logonURL;
    private String securityURL;
    private String authLoginURL;
    private String error;
    private Button mEmailSignInButton;

    private String token;
    private PerformNetworkOperations performNetworkOperations;
    private static LoginActivity instance;

    public static LoginActivity getInstance() {
        return instance;
    }

    public static void setInstance(LoginActivity instance) {
        LoginActivity.instance = instance;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setInstance(this);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEMSIpview = (EditText) findViewById(R.id.ems_ip);
        launchURL = getResources().getString(R.string.launch_url);
        getAuthURL = getResources().getString(R.string.auth_session);
        logonURL = getResources().getString(R.string.logon_url);
        securityURL = getResources().getString(R.string.security_url);
        authLoginURL = getResources().getString(R.string.auth_login);
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        performNetworkOperations = new PerformNetworkOperations(this);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        mEMSIpview.setError(null);
        // Store values at the time of the login attempt.
        final String emsIP = mEMSIpview.getText().toString();
        final String email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(emsIP)) {
            mEMSIpview.setError("Please enter an ems ip");
            focusView = mEMSIpview;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            performNetworkOperations.doLogin(emsIP, email, password);
//            Toast.makeText(this,token,Toast.LENGTH_SHORT).show();
//            List<String> nodeData = performNetworkOperations.getNodesList(emsIP,token);

        }
    }

    public void navigateActivity(List<String> nodeDataList, String emsIp) {
        View view = mEmailSignInButton;
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);

        Intent intent = new Intent(this, DemoLikeTumblrActivity.class);
        intent.putExtra(DemoLikeTumblrActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(DemoLikeTumblrActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putStringArrayListExtra("NodeData", (ArrayList<String>) nodeDataList);
        intent.putExtra("EMSIP", emsIp);
        ActivityCompat.startActivity(this, intent, options.toBundle());
    }

}

