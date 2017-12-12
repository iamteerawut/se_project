package com.example.pillbox;

public class TakeMedication {
    private int medId;
    private String medName;
    private String timeOfDay;
    private int dosage;
    private String beforeFood;



    public TakeMedication(int medId, String medName, String timeOfDay, int dosage, String beforeFood){
        this.medId = medId;
        this.medName = medName;
        this.timeOfDay = timeOfDay;
        this.dosage = dosage;
        this.beforeFood = beforeFood;
    }

    public int getMedId(){
        return medId;
    }

    public String getMedName(){
        return medName;
    }

    public String getTimeOfDay(){
        return timeOfDay;
    }

    public int getDosage(){
        return dosage;
    }

    public  String getBeforeFood(){
        return beforeFood;
    }

}



