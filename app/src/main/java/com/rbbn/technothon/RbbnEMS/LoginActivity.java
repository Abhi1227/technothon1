package com.rbbn.technothon.RbbnEMS;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rbbn.technothon.RbbnEMS.utils.PerformNetworkOperations;

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
    private UserLoginTask mAuthTask = null;

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

    private String token;
    private PerformNetworkOperations performNetworkOperations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mEMSIpview = (EditText) findViewById(R.id.ems_ip);
        launchURL = getResources().getString(R.string.launch_url);
        getAuthURL = getResources().getString(R.string.auth_session);
        logonURL = getResources().getString(R.string.logon_url);
        securityURL = getResources().getString(R.string.security_url);
        authLoginURL = getResources().getString(R.string.auth_login);
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
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
        if (mAuthTask != null) {
            return;
        }

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


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, String> {

        private final String mEmail;
        private final String mEmsIp;
        private final String mPassword;
        //        OkHttpClient client = getUnsafeOkHttpClient(myCookieJar);
        String resp;


        UserLoginTask(String emsIp, String email, String password) {
            mEmail = email;
            mPassword = password;
            mEmsIp = emsIp;
//            myCookie=myCookieJar;

        }

        @Override
        protected void onPreExecute() {
            Toast.makeText(LoginActivity.this, "https://" + mEmsIp + authLoginURL + mEmail + mPassword, Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

//            try {
//                // Simulate network access.
////                Thread.sleep(2000);
//                resp = getDataFromUrl("https://" + mEmsIp + securityURL,mEmail,mPassword);
//                return resp;
//            } catch (Exception e) {
//                return e.toString();
//            }

//            RequestBody requestBody = new MultipartBody.Builder()
////                    .setType(MultipartBody.FORM)
//                    .addFormDataPart("username", mEmail)
//                    .addFormDataPart("password", mPassword)
//                    .build();
////
//            Request request_security = new Request.Builder()
//                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
//                    .url("https://" + mEmsIp + authLoginURL)
//                    .post(requestBody)
//                    .build();
////
//            try {
//                Response response = client.newCall(request_security).execute();
////                return response.headers().get("Set-Cookie");
//                String s= response.body().string();
////                Log.d("Abhishek",response.headers().get("Set-Cookie"));
//                return s;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            Request request_logon = new Request.Builder()
//                    .url("https://" + mEmsIp + logonURL)
//                    .build();
//
//            try {
//                Response response = client.newCall(request_logon).execute();
////                return response.headers().get("Set-Cookie");
////                String s= response.body().string();
////                Log.d("Abhishek",response.headers().get("Set-Cookie"));
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Request request_launch = new Request.Builder()
//                    .url("https://" + mEmsIp + launchURL)
//                    .build();
//
//            try {
//                Response response = client.newCall(request_launch).execute();
////                return response.headers().get("Set-Cookie");
//
////                String s= response.body().string();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

//            Request request_getAuth = new Request.Builder()
//                    .url("https://" + mEmsIp + getAuthURL)
//                    .build();
//
//            try {
//                Response response = client.newCall(request_getAuth).execute();
//
//                String s= response.body().string();
//                return s;
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


            return null;

//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here.
//            return true;
        }

        @Override
        protected void onPostExecute(final String resp) {
            mAuthTask = null;
            showProgress(false);
            Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_SHORT).show();
            Log.d("Abhishek", resp);
//            Log.d("Abhishek", myCookieJar.showCookies());
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }


}

