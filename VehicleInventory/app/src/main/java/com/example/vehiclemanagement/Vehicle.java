package com.example.vehiclemanagement;

import java.io.Serializable;

/**
 * The purpose of this class is to store vehicle data for a single vehicle.
 * Class is data model of a vehicle.
 * @author Jamshid Nazari 17073524
 * @version 1.0
 */

public class Vehicle implements Serializable {
    private int vehicle_id;
    private String make;
    private String model;
    private int year;
    private int price;
    private String license_number;
    private String colour;
    private int number_doors;
    private String transmission;
    private int mileage;
    private String fuel_type;
    private int engine_size;
    private String body_style;
    private String condition;
    private String notes;

    /**
     * Constructor for Creating a vehicle instance.
     * @param vehicle_id unique Vehicle ID
     * @param make Vehicle Manufacturer like toyota
     * @param model Vehicle Model
     * @param year Year which vehicle was made in
     * @param price
     * @param license_number registeration in LLNNLLL format
     * @param colour
     * @param number_doors
     * @param transmission
     * @param mileage
     * @param fuel_type
     * @param engine_size
     * @param body_style
     * @param condition
     * @param notes
     */

    public Vehicle(int vehicle_id, String make, String model,
                   int year, int price, String license_number, String colour, int
                           number_doors, String transmission, int mileage, String fuel_type,
                   int engine_size, String body_style, String condition, String notes)
    {
        this.vehicle_id =  vehicle_id;
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
        this.license_number = license_number;
        this.colour = colour;
        this.number_doors = number_doors;
        this.transmission = transmission;
        this.mileage = mileage;
        this.fuel_type = fuel_type;
        this.engine_size = engine_size;
        this.body_style = body_style;
        this.condition = condition;
        this.notes = notes;
    }

    /**
     * Create empty Vehicle object
     */
    public Vehicle()
    {

    }


    //Getters and Setters

    /**
     * @return the vehicle_id
     */
    public int getVehicleId() {
        return vehicle_id;
    }

    /**
     * @return the make
     */
    public String getMake() {
        return make;
    }


    /**
     * @return the model
     */
    public String getModel() {
        return model;
    }


    /**
     * @return the year vehicle made
     */
    public int getYear() {
        return year;
    }


    /**
     * @return the price
     */
    public int getPrice() {
        return price;
    }


    /**
     * @return the license_number
     */
    public String getLicenseNumber() {
        return license_number;
    }


    /**
     * @return the colour
     */
    public String getColour() {
        return colour;
    }


    /**
     * @return the number_doors
     */
    public int getNumberDoors() {
        return number_doors;
    }


    /**
     * @return the transmission
     */
    public String getTransmission() {
        return transmission;
    }


    /**
     * @return the mileage
     */
    public int getMileage() {
        return mileage;
    }


    /**
     * @return the fuel_type
     */
    public String getFuelType() {
        return fuel_type;
    }


    /**
     * @return the engine_size
     */
    public int getEngineSize() {
        return engine_size;
    }


    /**
     * @return the body_style
     */
    public String getBodyStyle() {
        return body_style;
    }


    /**
     * @return the condition
     */
    public String getCondition() {
        return condition;
    }


    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }


    /**
     * @param vehicle_id the vehicle_id to set
     */
    public void setVehicleId(int vehicle_id) {
        this.vehicle_id = vehicle_id;
    }


    /**
     * @param make the make to set
     */
    public void setMake(String make) {
        this.make = make;
    }


    /**
     * @param model the model to set
     */
    public void setModel(String model) {
        this.model = model;
    }


    /**
     * @param year the year to set
     */
    public void setYear(int year) {
        this.year = year;
    }


    /**
     * @param price the price to set
     */
    public void setPrice(int price) {
        this.price = price;
    }


    /**
     * @param license_number the license_number to set
     */
    public void setLicenseNumber(String license_number) {
        this.license_number = license_number;
    }


    /**
     * @param colour the colour to set
     */
    public void setColour(String colour) {
        this.colour = colour;
    }


    /**
     * @param number_doors the number_doors to set
     */
    public void setNumberDoors(int number_doors) {
        this.number_doors = number_doors;
    }


    /**
     * @param transmission the transmission to set
     */
    public void setTransmission(String transmission) {
        this.transmission = transmission;
    }


    /**
     * @param mileage the mileage to set
     */
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }


    /**
     * @param fuel_type the fuel_type to set
     */
    public void setFuelType(String fuel_type) {
        this.fuel_type = fuel_type;
    }


    /**
     * @param engine_size the engine_size to set
     */
    public void setEngineSize(int engine_size) {
        this.engine_size = engine_size;
    }


    /**
     * @param body_style the body_style to set
     */
    public void setBodyStyle(String body_style) {
        this.body_style = body_style;
    }

    /**
     * @param condition the condition to set
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }


}
