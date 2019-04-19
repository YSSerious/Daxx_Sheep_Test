package com.paazl.dto;

public class SheepStatusesDto {
    private int numberOfHealthySheep;
    private int numberOfDeadSheep;

    public SheepStatusesDto() {}

    public SheepStatusesDto (
            int numberOfHealthySheep,
            int numberOfDeadSheep) {
        this.numberOfHealthySheep = numberOfHealthySheep;
        this.numberOfDeadSheep = numberOfDeadSheep;
    }

    public int getNumberOfHealthySheep() {
        return numberOfHealthySheep;
    }

    public void setNumberOfHealthySheep(int numberOfHealthySheep) {
        this.numberOfHealthySheep = numberOfHealthySheep;
    }

    public int getNumberOfDeadSheep() {
        return numberOfDeadSheep;
    }

    public void setNumberOfDeadSheep(int numberOfDeadSheep) {
        this.numberOfDeadSheep = numberOfDeadSheep;
    }


}