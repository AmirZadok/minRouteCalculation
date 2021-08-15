package com.finalproject3.amir.testgooglemaps;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GeoTask extends AsyncTask<String, Void, ArrayList<String>>
{
    ProgressDialog pd;
    protected Context mContext;
    protected int minCost;
    private TravelingSalesmanHeldKarp checkDis;
    private ArrayList Thepath;

    //constructor is used to get the context.
    public GeoTask(Context mContext) {
        this.mContext = mContext;
        checkDis = new TravelingSalesmanHeldKarp();
    }
    //This function is executed before before "doInBackground(String...params)" is executed to dispaly the progress dialog
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(mContext);
        pd.setMessage("Loading");
        pd.setCancelable(false);
        pd.show();
    }
    //This function is executed after the execution of "doInBackground(String...params)" to dismiss the dispalyed progress dialog and call "setDouble(Double)" defined in "MainActivity.java"
    @Override
    protected void onPostExecute(ArrayList<String> aDouble) {
        super.onPostExecute(aDouble);
        if(aDouble!=null)
        {
            pd.dismiss();
        }
        else
            Toast.makeText(mContext, "Error4!Please Try Again wiht proper values", Toast.LENGTH_SHORT).show();
    }



    @Override
    protected ArrayList<String> doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line=br.readLine();
                }
                String json=sb.toString();
                sb.setLength(0);
                Log.d("JSON", json);

                JSONObject root = new JSONObject(json);
                JSONArray dest_add=root.getJSONArray("destination_addresses");


                JSONArray array_rows=root.getJSONArray("rows");

                int distanceMetrix[][] = new int[array_rows.length()][array_rows.length()] ;
                for (int i = 0 ; i<array_rows.length();i++ )
                {
                    Log.d("JSON", "the place:" + dest_add.getString(i));
                    checkDis.AddLoction(dest_add.getString(i));
                    JSONObject object_rows = array_rows.getJSONObject(i);

                    JSONArray array_elements = object_rows.getJSONArray("elements");
                    int time;
                    for (int j = 0; j < array_elements.length(); j++)
                    {
                        JSONObject object_elements = array_elements.getJSONObject(j);
                        JSONObject object_duration = object_elements.getJSONObject("duration");
                        time = Integer.parseInt(object_duration.optString("value").toString()) / 60 ;
                        distanceMetrix[i][j] =time;
                    }
                }


              //  minCost = lio.minCost(distanceMetrix);
                Thepath=checkDis.getToureIntArr();
//                Log.d(" the tour is ",":" + Thepath.toString());

                return Thepath;

            }
        } catch (MalformedURLException e) {
            Log.d("error", "error1");
        } catch (IOException e) {
            Log.d("error", "error2");
        } catch (JSONException e) {
            Log.d("error","error3");
        }


        return null;
    }


}

