package com.se.pillminder.model;

public class Medication {
    private String medId;
    private String medName;
    private int dosage;
    private int frequency;
    private String beforeFood;
    private int amount;
    private String units;
    private String startDate;
    private String endDate;
    private int morning = 0;
    private int afternoon = 0;
    private int evening = 0;
    private int night = 0;

    public Medication(String medId, String medName, int dosage, int morning, int afternoon,
                      int evening, int night, String beforeFood, int amount, String units){
        this.medId = medId;
        this.medName = medName;
        this.dosage = dosage;
        this.beforeFood = beforeFood;
        this.amount = amount;
        this.units = units;

        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
    }

    public Medication(String medId, String medName, int dosage, int morning, int afternoon,
                      int evening, int night, String beforeFood, int amount, String units, String startDate, String endDate){
        this.medId = medId;
        this.medName = medName;
        this.dosage = dosage;
        this.morning = morning;
        this.afternoon = afternoon;
        this.evening = evening;
        this.night = night;
        this.beforeFood = beforeFood;
        this.amount = amount;
        this.units = units;
        this.startDate = startDate;
        this.endDate = endDate;

    }



    public String getMedId(){
        return medId;
    }

    public String getMedName(){
        return medName;
    }

    public int getDosage(){
        return dosage;
    }


    public String getBeforeFood(){
        return beforeFood;
    }

    public int getAmount(){
        return amount;
    }

    public String getUnits(){
        return units;
    }

    public String getStartDate(){
        return startDate;
    }

    public String getEndDate(){
        return endDate;
    }

    public  int getMorning(){
        return morning;
    }

    public int getAfternoon(){
        return afternoon;
    }

    public int getEvening(){
        return evening;
    }

    public int getNight(){
        return night;
    }

}
