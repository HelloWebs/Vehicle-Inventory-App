package com.example.vehiclemanagement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> listVehicles = new ArrayList<String>() ;
    ArrayList<Vehicle> vehicles ;
    ListView vehicleList;
    ArrayAdapter<String> arrayAdapter;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(null);
        setSupportActionBar(toolbar); // use a custom toolbar

        //Get reference to listView
        vehicleList = findViewById(R.id.list_thing);

        //Create a Array Adapter for listview to manage its content and view, pass context and type of list
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);

        //set adapter to newly createed adapter
        vehicleList.setAdapter(arrayAdapter);

        //AsyncTask  - connect to the server and set the data in the listview
        new FetchVehiclesTask().execute();


        //Click handler for each of the list add a click handler
        vehicleList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // create Intent to DetailDisplay
                Intent intent = new Intent(getApplicationContext(), DetailedDisplay.class);

                // Add the vehicle that was clicked to the intent package
                intent.putExtra("vehicle", vehicles.get(position));

                //Start the detailed display vehicle activity (DetailedDisplay.class)
                startActivity(intent);
            }
        });


        //Long Hold on each item in the ListView
        vehicleList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Log.i("TAG", "onItemLongClick: ");
                // make an confirmation box
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Delete Vehicle")
                        .setMessage("Are you sure you want to delete vehicle?")
                        //set the yes button
                        .setPositiveButton("Of course", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Start the Background Task to delete the vehicle from the database.
                                new DeleteVehicleTask().execute(vehicles.get(position).getVehicleId());
                            }
                        }).show();
                return true;
            }
        });

        //set listeners for toolbar buttons
        toolbar.findViewById(R.id.vehicle_add_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //change activity to add vehicle activity
                Intent addVehicleIntent = new Intent(MainActivity.this, DetailedEdit.class);
                startActivity(addVehicleIntent);
            }
        });

        // Pull to refresh
        swipeRefreshLayout = findViewById(R.id.pullRefresh);

        //Set listener for pull to refresh
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new FetchVehiclesTask().execute();

            }
        });

    }

    //Handle activity changes
    @Override
    protected void onResume() {
        new FetchVehiclesTask().execute();
        super.onResume();
    }

    /**
     * This class fetches all vehicles from the Web server in the background using AsyncTask and updates the ListView accordingly.
     */

    public class FetchVehiclesTask extends AsyncTask<Void,Void,JSONArray>
    {
        @Override
        protected JSONArray doInBackground(Void... voids) {

            JSONArray jsonArray = null;
            try {
                //Create a URL Object with URL from constants class
                URL url = new URL(Constants.VEHICLE_API_GET_URL);

                //Open Connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //Set the HTTP method
                urlConnection.setRequestMethod("GET");

                //Open Stream to Connection and Read the data with Scanner Util
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner scanner = new Scanner(in).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";

                //Parse Json data from raw server data
                jsonArray = new JSONArray(response);
                in.close();
            }
            catch(IOException | JSONException e) { return null;}
            return jsonArray;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            if(jsonArray==null|| jsonArray.length()==0)
                return;

            listVehicles = new ArrayList<>() ;
            vehicles = new ArrayList<>();
            try {
                // recreate all the Vehicle objects fron JSON that was recieved
                for (int i = 0; i < jsonArray.length(); i++) {
                    Vehicle vehicle = new Vehicle();
                    //set the corresponding values in the temporary vehicle from the json object
                    vehicle.setVehicleId((int) jsonArray.getJSONObject(i).get("vehicle_id"));
                    vehicle.setMake( jsonArray.getJSONObject(i).get("make").toString() );
                    vehicle.setModel( jsonArray.getJSONObject(i).get("model").toString() );
                    vehicle.setYear((int) jsonArray.getJSONObject(i).get("year"));
                    vehicle.setPrice((int) jsonArray.getJSONObject(i).get("price"));
                    vehicle.setLicenseNumber( jsonArray.getJSONObject(i).get("license_number").toString() );
                    vehicle.setColour( jsonArray.getJSONObject(i).get("colour").toString() );
                    vehicle.setNumberDoors((int) jsonArray.getJSONObject(i).get("number_doors"));
                    vehicle.setTransmission( jsonArray.getJSONObject(i).get("transmission").toString() );
                    vehicle.setMileage((int) jsonArray.getJSONObject(i).get("mileage"));
                    vehicle.setFuelType( jsonArray.getJSONObject(i).get("fuel_type").toString() );
                    vehicle.setEngineSize((int) jsonArray.getJSONObject(i).get("engine_size"));
                    vehicle.setBodyStyle( jsonArray.getJSONObject(i).get("body_style").toString() );
                    vehicle.setCondition( jsonArray.getJSONObject(i).get("condition").toString() );
                    vehicle.setNotes( jsonArray.getJSONObject(i).get("notes").toString() );
                    vehicles.add(vehicle);

                    //add the vehicle description to the  listview data source
                    listVehicles.add(vehicle.getMake() + " "+vehicle.getModel()+ " ("+vehicle.getYear()+") \n"+vehicle.getLicenseNumber());
                }
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
            //Clear the previous ListView data
            arrayAdapter.clear();

            //Add the new data
            arrayAdapter.addAll(listVehicles);

            //notify the listview that its datasets has been changed
            arrayAdapter.notifyDataSetChanged();

            // stop the loading indicator for pull to refresh
            swipeRefreshLayout.setRefreshing(false);


        }
    }

    public class DeleteVehicleTask extends AsyncTask<Integer,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Integer... integers) {
            try {
                if(integers[0]==null)
                    return  false;
                //create parameters that will be sent to server
                StringBuilder getParams  =new StringBuilder().append("&").append("vehicle_id=").append(integers[0]);
                // connect to the URL with the parameters
                URL url = new URL(Constants.VEHICLE_API_GET_URL + getParams.toString());
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                //set the Method
                urlConnection.setRequestMethod("DELETE");

                // get Stream to the http connection
                InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                Scanner scanner = new Scanner(in).useDelimiter("\\A");

                //read the response
                String response = scanner.hasNext() ? scanner.next() : "";
                in.close();
                //check if the request was successful by looking for "success" in the response and 200 response code
                if(response.contains("success") && urlConnection.getResponseCode()==200) {
                    return true;
                }
                // indicate to postExcute that operation was unsuccessful
                return false;

            } catch (IOException e) {
                e.printStackTrace();
            }
            return  false;
        }

        @Override
        protected void onPostExecute(Boolean status) {
            if(status)
            {
                Toast.makeText(MainActivity.this,"Vehicle Deleted!",Toast.LENGTH_LONG).show();
                //update the list
                new FetchVehiclesTask().execute();

            }
            else{
                Toast.makeText(MainActivity.this,"Error!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
