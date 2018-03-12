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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.CookieJar;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
    private String error;
    private MyCookieJar myCookieJar;

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
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        myCookieJar = new MyCookieJar();
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

        if (TextUtils.isEmpty(emsIP)){
            mEMSIpview.setError("Please enter an ems ip");
            focusView=mEMSIpview;
            cancel=true;
        }
        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(password) ) {
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
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            mAuthTask = new UserLoginTask(emsIP, email, password);
            mAuthTask.execute((Void) null);
            Toast.makeText(LoginActivity.this, "https://" + emsIP + launchURL, Toast.LENGTH_LONG).show();
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://" + emsIP + logonURL,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            // Display the first 500 characters of the response string.
//                            Toast.makeText(LoginActivity.this, "Response is: " + response.substring(0, 500).toString(), Toast.LENGTH_LONG).show();
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.e("abhsihek", error.toString());
//                    Toast.makeText(LoginActivity.this, "Error is: " + error.toString(), Toast.LENGTH_LONG).show();
//                }
//
//                public String getBodyContentType() {
//                    return "application/x-www-form-urlencoded; charset=UTF-8";
//                }
//
//
//                protected Map<String, String> getParams() throws AuthFailureError {
//                    Map<String, String> params = new HashMap<String, String>();
//                    params.put("j_username", email.trim());
//                    params.put("j_password", password.trim());
//                    params.put("j_security_check", "+Log+In+");
//                    return params;
//                }
//
//            });
//            MySingleton.getInstance(this).addToRequestQueue(stringRequest);


        }
    }

    private String getDataFromUrl(String demoIdUrl) {
//        new NukeSSLCerts().nuke();
        String result = null;
        int resCode;
        InputStream in;
        try {
            URL url = new URL(demoIdUrl);
            URLConnection urlConn = url.openConnection();

            HttpURLConnection httpsConn = (HttpURLConnection) urlConn;
            httpsConn.setAllowUserInteraction(false);
//            httpsConn.setInstanceFollowRedirects(false);
            httpsConn.setRequestMethod("GET");

            httpsConn.connect();
            resCode = httpsConn.getResponseCode();
            Toast.makeText(LoginActivity.this, resCode, Toast.LENGTH_SHORT).show();
            if (resCode == HttpURLConnection.HTTP_OK) {
                in = httpsConn.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        in, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                in.close();
                result = sb.toString();
                Log.d("abhsihek", result);
            } else {
                error += resCode;
                return error;
//                Log.d("abhsihek",error);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
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
        OkHttpClient client = getUnsafeOkHttpClient();
        String resp;

        UserLoginTask(String emsIp, String email, String password) {
            mEmail = email;
            mPassword = password;
            mEmsIp = emsIp;

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

//            try {
//                // Simulate network access.
////                Thread.sleep(2000);
//                resp = getDataFromUrl("https://" + mEmsIp + launchURL);
//            } catch (Exception e) {
//                return false;
//            }

            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("j_username", mEmail)
                    .addFormDataPart("j_password", mPassword)
                    .addFormDataPart("j_security_check", "+Log+In+")
                    .build();

            Request request = new Request.Builder()
                    .addHeader("Content-Type", " application/x-www-form-urlencoded")
                    .url("https://" + mEmsIp + securityURL)
                    .post(requestBody)
                    .build();

            try {
                Response response = client.newCall(request).execute();

//                String s= response.body().string();

            } catch (Exception e) {
                e.printStackTrace();
            }


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
//            mAuthTask = null;
//            showProgress(false);
            Toast.makeText(LoginActivity.this, resp, Toast.LENGTH_SHORT).show();
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

//    public static class NukeSSLCerts {
//        protected static final String TAG = "NukeSSLCerts";
//
//        public static void nuke() {
//            try {
//                TrustManager[] trustAllCerts = new TrustManager[]{
//                        new X509TrustManager() {
//                            public X509Certificate[] getAcceptedIssuers() {
//                                X509Certificate[] myTrustedAnchors = new X509Certificate[0];
//                                return myTrustedAnchors;
//                            }
//
//                            @Override
//                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
//                            }
//
//                            @Override
//                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
//                            }
//                        }
//                };
//
//                SSLContext sc = SSLContext.getInstance("SSL");
//                sc.init(null, trustAllCerts, new SecureRandom());
//                HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//                    @Override
//                    public boolean verify(String arg0, SSLSession arg1) {
//                        return true;
//                    }
//                });
//            } catch (Exception e) {
//            }
//        }
//    }

    private static OkHttpClient getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain,
                                                       String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[0];
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            return new OkHttpClient.Builder()
                    .sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier(new HostnameVerifier() {
                        @Override
                        public boolean verify(String hostname, SSLSession session) {
                            return true;
                        }
                    }).followRedirects(false)
                    .followSslRedirects(true)
                    .cookieJar(new MyCookieJar())
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

