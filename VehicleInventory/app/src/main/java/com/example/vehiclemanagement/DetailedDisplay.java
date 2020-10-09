package com.example.vehiclemanagement;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class DetailedDisplay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_display);

        //custom toobar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        // Get the intent that  was used to call this activity
        Intent intent = getIntent();

        // get the vehicle that was sent through int
        final Vehicle vehicle = (Vehicle) intent.getSerializableExtra("vehicle");

        if(vehicle== null)
        {
            finish();
        }

        // Get Reference to all the textview in the xml
        // set each one to the corresponding vehicle data.

        // description contains  3 vehicle attibutes in one
        TextView textViewVehicleDescription = findViewById(R.id.textViewVehicleDescription);
        String description = vehicle.getMake() + " "+ vehicle.getModel() + " ("+vehicle.getYear()+")";
        textViewVehicleDescription.setText(description);


        TextView textViewLicenseNumber = findViewById(R.id.textViewLicense_number);
        textViewLicenseNumber.setText(vehicle.getLicenseNumber());
        
        TextView textViewPrice = findViewById(R.id.textViewPrice);
        textViewPrice.setText(vehicle.getPrice()+"");
       
        TextView textViewColour = findViewById(R.id.textViewColour);
        textViewColour.setText(vehicle.getColour());
       
        TextView textViewTransmission = findViewById(R.id.textViewTransmission);
        textViewTransmission.setText(vehicle.getTransmission());
       
        TextView textViewMileage = findViewById(R.id.textViewMileage);
        textViewMileage.setText(vehicle.getMileage()+"");
       
        TextView textViewFuelType = findViewById(R.id.textViewFuel_type);
        textViewFuelType.setText(vehicle.getFuelType());

        TextView textViewEngineSize = findViewById(R.id.textViewEngine_size);
        textViewEngineSize.setText(vehicle.getEngineSize()+"");

        TextView textViewNumberDoors = findViewById(R.id.textViewNumber_doors);
        textViewNumberDoors.setText(vehicle.getNumberDoors()+"");

        TextView textViewCondition = findViewById(R.id.textViewCondition);
        textViewCondition.setText(vehicle.getCondition());

        TextView textViewNotes = findViewById(R.id.textViewNotes);
        textViewNotes.setText(vehicle.getNotes());


        //Set the click handler to edit button
        toolbar.findViewById(R.id.vehicle_edit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent to the detailEdit Avtivity
                Intent addVehicleIntent = new Intent(DetailedDisplay.this, DetailedEdit.class);

                //send the vehicle that we are looking at.
                addVehicleIntent.putExtra("vehicle",vehicle);

                //start
                startActivity(addVehicleIntent);
            }
        });

        // handler for home button on the tool bar
        toolbar.findViewById(R.id.vehicle_show_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //simple intent and start activity to Main activity
                startActivity(new Intent(DetailedDisplay.this, MainActivity.class));
            }
        });

    }

}
