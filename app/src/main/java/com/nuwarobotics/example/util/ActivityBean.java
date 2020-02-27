package com.nuwarobotics.example.util;

public class ActivityBean {
    private String label;
    private String name;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "label:" + label + ","
                + "name:" + name;
    }
}
