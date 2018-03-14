package com.rbbn.technothon.RbbnEMS.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.rbbn.technothon.RbbnEMS.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * Created by aaagarwal on 14-03-2018.
 */

public class PerformNetworkOperations {
    Context ctx;
    String token;
    private String authLoginURL;
    protected String fetchNodesURL;
    private String dataResponse;


    public PerformNetworkOperations(Context ctx) {
        this.ctx = ctx;
        authLoginURL = ctx.getResources().getString(R.string.auth_login);
        fetchNodesURL = ctx.getResources().getString(R.string.fetch_nodes);
    }

    public void doLogin(final String emsIP, final String username, final String password) {

        String authlogin_url = gethttpsURL(emsIP, authLoginURL);

//      Toast.makeText(ctx, authlogin_url, Toast.LENGTH_LONG).show();

        StringRequest postRequest = new StringRequest(Request.Method.POST, authlogin_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        NetworkUtils.getInstance(ctx).hidepDialog();
                        Log.d("Abhishek", response);
                        try {
                            JSONObject myresponse = new JSONObject(response);
                            token = myresponse.getString("token");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (token == null) {
                            Toast.makeText(ctx, "Unable to fetch Login Token", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(ctx, "Successfully Logged In" + token, Toast.LENGTH_LONG).show();
                            getNodesList(emsIP, token);
                            Log.d("Abhishek", token);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            showProgress(false);
                        // error
                        NetworkUtils.getInstance(ctx).hidepDialog();
                        Log.d("Abhishek", String.valueOf(error.networkResponse.statusCode));
                        Toast.makeText(ctx, "Invalid UserName or Password", Toast.LENGTH_LONG).show();

                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };

        NetworkUtils.getInstance(ctx).showpDialog();
        NetworkUtils.getInstance(ctx).addToRequestQueue(postRequest);


//        Toast.makeText(ctx,token,Toast.LENGTH_SHORT).show();


    }

    public List<String> getNodesList(String emsIp, final String mytoken) {
        String getNodesUrl = gethttpsURL(emsIp, fetchNodesURL);

        StringRequest nodeRequest = new StringRequest(Request.Method.GET, getNodesUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("Abhishek", response);
                        try {
                            List<String> myList = new ArrayList<String>();
                            JSONObject responseJson = new JSONObject(response);
                            JSONArray myresponse = responseJson.getJSONArray("nodes");
                            for (int i = 0; i < myresponse.length(); i++) {
                                JSONObject jsonObject = myresponse.getJSONObject(i);
                                myList.add(jsonObject.getString("name"));
                                Log.d("Abhishek", myList.toString());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                            showProgress(false);
                        // error
//                        Log.d("Abhishek", String.valueOf(error.networkResponse.statusCode));
                        Toast.makeText(ctx, "Unable to get Node list", Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", mytoken);
                return params;
            }
        };

        // Adding request to request queue
        NetworkUtils.getInstance(ctx).addToRequestQueue(nodeRequest);
        return null;
    }

    public String gethttpsURL(String emsIp, String url) {
        return "https://" + emsIp + url;
    }

    class WaitforTask extends AsyncTask<CountDownLatch, Void, String> {

        @Override
        protected String doInBackground(CountDownLatch... params) {
            try {
                params[0].await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

//                return token;
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}
