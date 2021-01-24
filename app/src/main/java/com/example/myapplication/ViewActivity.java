package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewActivity extends HeaderActivity {
    AnyChartView anyChartView;
    TextView textViewName;
    TextView textViewCode;
    Button buttonWa;
    Button buttonLoad;
    String d_name, d_code,d_id;
    int count;
    float humidity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        setHeaderView();

        anyChartView = (AnyChartView) findViewById(R.id.any_chart_view);
        textViewName = (TextView) findViewById(R.id.textViewItem);
        textViewCode= (TextView) findViewById(R.id.textViewNote);
        buttonWa = (Button) findViewById(R.id.buttonWa);
        buttonLoad = (Button) findViewById(R.id.buttonLoad);

        SharedPreferences sp=this.getSharedPreferences("Device", MODE_PRIVATE);
        d_name = sp.getString("name", null);
        d_code = sp.getString("code", null);
        d_id = sp.getString("id", null);

        textViewName.setText(d_name);
        textViewCode.setText(d_code);
        System.out.println("Device "+d_name+d_code+d_id);

        count = 0;
        humidity = 0;
        getInfo();


        buttonLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivityForResult(getIntent(),0);

            }
        });

        buttonWa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendWaRequest();
            }
        });
    }
    public void setUpPieChart(){
        Pie pie = AnyChart.pie();
        String[] name = {" ","Water"};
        humidity = Math.max(humidity,0.05f);
        System.out.println("Humidity1 "+humidity);
        float[] percent = {1-humidity,humidity};
        List<DataEntry> dataEntries = new ArrayList<>();
        for(int i=0; i<name.length; i++){
            dataEntries.add(new ValueDataEntry(name[i],percent[i]));
        }
        pie.data(dataEntries);
        anyChartView.setChart(pie);
    }


    public void getInfoRespones(String response){
        try {
            JSONObject obj = new JSONObject(response);
            System.out.println("Response "+obj.toString());
            if (obj.get("status").equals("1") && count++ <5){
                getInfo();
            }else {
                humidity =  Float.parseFloat(obj.get("humidity").toString());
                System.out.println("Humidity "+humidity);
                setUpPieChart();
            }
//            System.out.println("Respones "+obj.toString());
//            listDevices = new JSONArray(obj.get("data").toString());
//            CustomAdapter customAdapter = new CustomAdapter(this, listDevices);
//            simpleList.setAdapter(customAdapter);
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }
    }

    public void getInfo() {
        String url = MainActivity.url+"/deviceInfo";
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String token = sp.getString("token", null);
        System.out.println("Token "+token);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("deviceId", d_id);
        JSONObject jsonBody = new JSONObject(postParam);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    getInfoRespones(response);
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


    public void getWaRespones(String response){
        try {
            JSONObject obj = new JSONObject(response);
            Toast.makeText(getApplicationContext(), obj.get("message").toString(),Toast.LENGTH_SHORT).show();
        } catch (Throwable t) {
            Log.e("My App", "Could not parse malformed JSON: \"" + response + "\"");
        }
    }

    public void sendWaRequest() {
        String url = MainActivity.url+"/command";
        SharedPreferences sp=this.getSharedPreferences("Login", MODE_PRIVATE);
        String token = sp.getString("token", null);
        System.out.println("Token "+token);
        Map<String, String> postParam= new HashMap<String, String>();
        postParam.put("deviceId", d_id);
        JSONObject jsonBody = new JSONObject(postParam);
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url
                    , new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.i("VOLLEY", response);
                    getWaRespones(response);
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