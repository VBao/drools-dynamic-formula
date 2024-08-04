package com.VBao.droolsDynamicFormula;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class SalaryFormulaVariable {
    private Double baseSalary;
    private Double realDayShiftWorkingHours;
    private Double realNightShiftWorkingHours;
    private Double standardWorkingTime;


    private Double dayShiftSalary = 0.0;
    private Double nightShiftSalary = 0.0;
    private Double finalShiftSalary = 0.0;

}
