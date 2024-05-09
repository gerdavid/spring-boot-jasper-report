package com.employee.utils;


import net.sf.jasperreports.engine.JRDefaultScriptlet;

public class BonusCalculator {

    /**
 * Calculates the bonus for an employee based on their salary.
 * The bonus is calculated as 10% of the employee's salary.
 *
 * @param salary the employee's salary
 * @return the bonus amount
 */
public static Double calculateBonus(Double salary) {
    // Calculate bonus (e.g., 10% of salary)
    return salary * 0.10;
}
}
