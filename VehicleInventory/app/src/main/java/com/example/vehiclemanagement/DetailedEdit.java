package com.example.vehiclemanagement;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Scanner;

public class DetailedEdit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");

        setSupportActionBar(toolbar);

        // Get the intent that  was used to call this activity
        Intent intent = getIntent();

        // get the vehicle that was sent through int
        final Vehicle vehicle = (Vehicle) intent.getSerializableExtra("vehicle");

        // get references to all the inputTexts through Id
        final TextInputEditText inputTextMake = findViewById(R.id.editTextMake);
        final TextInputEditText inputTextModel = findViewById(R.id.editTextModel);
        final TextInputEditText inputTextYear = findViewById(R.id.editTextYear);
        final TextInputEditText inputTextPrice = findViewById(R.id.editTextPrice);
        final TextInputEditText inputTextLicense_number = findViewById(R.id.editTextLicense_number);
        final TextInputEditText inputTextColour = findViewById(R.id.editTextColour);
        final TextInputEditText inputTextNumber_doors = findViewById(R.id.editTextNumber_doors);
        final TextInputEditText inputTextTransmission = findViewById(R.id.editTextTransmission);
        final TextInputEditText inputTextMileage = findViewById(R.id.editTextMileage);
        final TextInputEditText inputTextFuel_type = findViewById(R.id.editTextFuel_type);
        final TextInputEditText inputTextEngine_size = findViewById(R.id.editTextEngine_size);
        final TextInputEditText inputTextBody_style = findViewById(R.id.editTextBody_style);
        final TextInputEditText inputTextCondition = findViewById(R.id.editTextCondition);
        final TextInputEditText inputTextNotes = findViewById(R.id.editTextNotes);

        //vehicle is passed in through intent if user wants to edit Otherwise they are adding a new vehicle
        if(vehicle != null)
        {
            // If the  user wants to edit a vehicle then populate the fields
            inputTextMake.setText(vehicle.getMake());
            inputTextModel.setText(vehicle.getModel());
            inputTextYear.setText(vehicle.getYear()+"");
            inputTextPrice.setText(vehicle.getPrice()+"");
            inputTextLicense_number.setText(vehicle.getLicenseNumber());
            inputTextColour.setText(vehicle.getColour());
            inputTextNumber_doors.setText(vehicle.getNumberDoors()+"");
            inputTextTransmission.setText(vehicle.getTransmission());
            inputTextMileage.setText(vehicle.getMileage()+"");
            inputTextFuel_type.setText(vehicle.getFuelType());
            inputTextEngine_size.setText(vehicle.getEngineSize()+"");
            inputTextBody_style.setText(vehicle.getBodyStyle());
            inputTextCondition.setText(vehicle.getCondition());
            inputTextNotes.setText(vehicle.getNotes());
        }

        //Save button in toolbar
        toolbar.findViewById(R.id.vehicle_save_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vehicle vehicleInput = new Vehicle();
                try {
                    //Get the text for each individual inputText then Set it in the corresponding vehicle data
                    vehicleInput.setMake(inputTextMake.getText().toString());
                    vehicleInput.setModel(inputTextModel.getText().toString());
                    vehicleInput.setYear(Integer.parseInt(inputTextYear.getText().toString()));
                    vehicleInput.setPrice(Integer.parseInt(inputTextPrice.getText().toString()));
                    vehicleInput.setLicenseNumber(inputTextLicense_number.getText().toString());
                    vehicleInput.setColour(inputTextColour.getText().toString());
                    vehicleInput.setNumberDoors(Integer.parseInt(inputTextNumber_doors.getText().toString()));
                    vehicleInput.setTransmission(inputTextTransmission.getText().toString());
                    vehicleInput.setMileage(Integer.parseInt(inputTextMileage.getText().toString()));
                    vehicleInput.setFuelType(inputTextFuel_type.getText().toString());
                    vehicleInput.setEngineSize(Integer.parseInt(inputTextEngine_size.getText().toString()));
                    vehicleInput.setBodyStyle(inputTextBody_style.getText().toString());
                    vehicleInput.setCondition(inputTextCondition.getText().toString());
                    vehicleInput.setNotes(inputTextNotes.getText().toString());
                }catch (NumberFormatException e)
                {
                    //Tell the user they have invalid field.
                    Toast.makeText(DetailedEdit.this,"Error Invalid number field!",Toast.LENGTH_SHORT).show();
                }

                //check if we are making a post request or put request
                // if intent has a vehicle then its an update request else its a new vehicle
                if(vehicle == null)
                {
                    new AddVehiclesTask().execute(vehicleInput);
                }
                else
                {
                    vehicleInput.setVehicleId(vehicle.getVehicleId());
                    new UpdateVehiclesTask().execute(vehicleInput);
                }


            }
        });

        //  Goto Main activity  toolbar button
        toolbar.findViewById(R.id.vehicle_show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // redirect to the main activity
                startActivity(new Intent(DetailedEdit.this, MainActivity.class));
            }
        });
    }


    /**
     * Class for Adding vehicle by making a post request
     */
    public class AddVehiclesTask extends AsyncTask<Vehicle,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Vehicle... v) {
            HttpURLConnection urlConnection;
            Vehicle vehicle = v[0];

            try {
                URL url = new URL(Constants.VEHICLE_API_BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //Get an Input stream to the connection
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");

                //Convert the Vehicle in to JSONObject
                String vehicleRequestData= DetailedEdit.vehicleToRequestData(vehicle);

                //Send the post parameters
                writer.write(vehicleRequestData);

                //flush any data in the stream
                writer.flush();

                // pass the connection to check for status and server message
                return DetailedEdit.checkConnectionStatus(urlConnection);


            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                //Notify the user that the operation was successful
                Toast.makeText(DetailedEdit.this, "Vehicle Added!",Toast.LENGTH_SHORT).show();
                // goto the previous activity
                finish();
            }
        }
    }


    /**
     * Class updates a vehicle by making a put request to server.
     */
    public class UpdateVehiclesTask extends AsyncTask<Vehicle,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Vehicle... v) {
            HttpURLConnection urlConnection;
            Vehicle vehicle = v[0];

            try {
                URL url = new URL(Constants.VEHICLE_API_BASE_URL);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("PUT");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);

                //Get an Input stream to the connection
                OutputStreamWriter writer = new OutputStreamWriter(urlConnection.getOutputStream(), "UTF-8");

                //Convert the Vehicle in to JSONObject
                String vehicleRequestData= DetailedEdit.vehicleToRequestData(vehicle);

                Log.i("TAGG", "doInBackground: "+ vehicleRequestData);
                //Send the post parameters
                writer.write(vehicleRequestData);

                //flush any data in the stream
                writer.flush();

                // pass the connection to check for status and server message
                return DetailedEdit.checkConnectionStatus(urlConnection);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if(result)
            {
                //Notify the user that the operation was successful
                Toast.makeText(DetailedEdit.this, "Vehicle Update!",Toast.LENGTH_SHORT).show();
                //Send the user to main page
                startActivity(new Intent(DetailedEdit.this, MainActivity.class));
            }
        }
    }


    /**
     * Convert Vehicle object to URL encoded Data for post and put request
     * @param vehicle
     * @return String object of the encoded data for adding and updating URL
     */

    public static String vehicleToRequestData(Vehicle vehicle)
    {
        JSONObject jsonVehicle = new JSONObject();
        StringBuilder urlData;
        try {
            // save the corresponding
            jsonVehicle.put("vehicle_id",vehicle.getVehicleId());
            jsonVehicle.put("make", vehicle.getMake());
            jsonVehicle.put("model", vehicle.getModel());
            jsonVehicle.put("year", vehicle.getYear());
            jsonVehicle.put("price", vehicle.getPrice());
            jsonVehicle.put("license_number", vehicle.getLicenseNumber());
            jsonVehicle.put("colour", vehicle.getColour());
            jsonVehicle.put("number_doors", vehicle.getNumberDoors());
            jsonVehicle.put("transmission", vehicle.getTransmission());
            jsonVehicle.put("mileage", vehicle.getMileage());
            jsonVehicle.put("fuel_type", vehicle.getFuelType());
            jsonVehicle.put("engine_size", vehicle.getEngineSize());
            jsonVehicle.put("body_style", vehicle.getBodyStyle());
            jsonVehicle.put("condition", vehicle.getCondition());
            jsonVehicle.put("notes", vehicle.getNotes());

            urlData = new StringBuilder();

            // Add the api to the body params
            urlData.append(URLEncoder.encode("apikey","UTF-8"))
                    .append("=")
                    .append(URLEncoder.encode(Constants.API_KEY,"UTF-8"));

            //Add the vehicle Data to params
            urlData.append("&json")
                    .append("=")
                    .append(URLEncoder.encode(jsonVehicle.toString(),"UTF-8"));
        }
        catch (JSONException | UnsupportedEncodingException e)
        {
            e.printStackTrace();
            return "";
        }

        return urlData.toString();

    }

    /**
     * Checks the response of the http connectiom, see if its successfull and the response code is OK
     * @param urlConnection the connection to check
     * @return  true - if connection is OK and the server is send "success"
     * @throws IOException  throws if there is an issue with the Connection or parsing server data
     */
    public static boolean checkConnectionStatus(HttpURLConnection urlConnection) throws IOException {
        InputStream in = new BufferedInputStream(urlConnection.getInputStream());
        Scanner scanner = new Scanner(in).useDelimiter("\\A");
        String response = scanner.hasNext() ? scanner.next() : "";
        in.close();

        //Check if the Http Response Code  is OK (200)
        if (response.contains("success") && urlConnection.getResponseCode() == 200) {
            return true;
        }
        return false;
    }

}
