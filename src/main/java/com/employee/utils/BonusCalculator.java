package com.employee.utils;


import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class BonusCalculator {

    public static Double calculateBonus (Double salary){
        // Calculate bonus (e.g., 10% of salary)
        return salary * 0.10;
    }
}
