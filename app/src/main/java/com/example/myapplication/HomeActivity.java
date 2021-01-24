package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends HeaderActivity {
    ListView simpleList;
    JSONArray listDevices;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setHeaderView();
        getDeviecs();
        simpleList = (ListView) findViewById(R.id.simpleListView);
        simpleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                JSONObject device = null;
                String d_name = null;
                String d_code = null;
                String d_id = null;
                try {
                    device = (JSONObject) listDevices.get(position);
                    d_name = (String) device.get("device_name");
                    d_code = (String) device.get("device_code");
                    d_id = String.valueOf(device.get("device_id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SharedPreferences sp=getSharedPreferences("Device", 0);
                SharedPreferences.Editor Ed=sp.edit();
                Ed.putString("name",d_name);
                Ed.putString("code",d_code);
                Ed.putString("id",d_id);
                Ed.apply();

                Intent intent = new Intent(getApplicationContext(), ViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void getDevicesRespones(String respone){
        try {
            JSONObject obj = new JSONObject(respone);
            System.out.println("Respones "+obj.toString());
            listDevices = new JSONArray(obj.get("data").toString());
            CustomAdapter customAdapter = new CustomAdapter(this, listDevices);
            simpleList.setAdapter(customAdapter);
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + respone + "\"");
        }
    }

    public void getDeviecs() {
        String url = MainActivity.url+"/listDeviceController";
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String token = sp.getString("token", null);
        System.out.println("Token "+token);
        JSONObject jsonBody = new JSONObject();
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    getDevicesRespones(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                    System.out.println(error.getMessage());
                    Toast.makeText(getApplicationContext(), "Connect Error",Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }
                @Override
                public byte[] getBody()  {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");
                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    HashMap<String, String> header = new HashMap<>();
                    header.put("Content-Type","application/json; charset=utf-8");
                    header.put("Authorization","Bearer "+token);
                    return header;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(10000,1,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Connect Error",Toast.LENGTH_SHORT).show();
        }
    }
}