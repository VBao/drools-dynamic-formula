package com.VBao.droolsDynamicFormula;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.springframework.web.bind.annotation.*;

@RestController("/drool")
@RequiredArgsConstructor
public class DroolController {
    private final KieContainer kieContainer;
    private final Formula shiftFormula = new Formula();

    @GetMapping()
    public Formula getShiftFormula() {
        return shiftFormula;
    }

    @PostMapping("/setFormula")
    public String setFormula(
            @RequestBody
            @Schema(example = """
                    {
                      "dayShiftFormula": "BaseSalary * (RealDayShiftWorkingHours - StandardWorkingTime)",
                      "nightShiftFormula": "BaseSalary * (RealNightShiftWorkingHours - StandardWorkingTime) * 1.5",
                      "shiftSalaryFormula": "DayShiftSalary + NightShiftSalary"
                    }
                    """)
            SalaryFormula dayShiftFormula) {

        shiftFormula.setDayFormula(dayShiftFormula.getDayShiftFormula());
        shiftFormula.setNightFormula(dayShiftFormula.getNightShiftFormula());
        shiftFormula.setTotalShiftFormula(dayShiftFormula.getShiftSalaryFormula());
        return "Shift formula is set";
    }

    @PostMapping("/calculate")
    public String calculate(
            @RequestBody
            @Schema(example = """
                    {
                      "baseSalary": 10000000,
                      "realDayShiftWorkingHours": 27,
                      "realNightShiftWorkingHours": 28,
                      "standardWorkingTime": 26
                    }
                    """)
            SalaryFormulaVariable salaryFormulaVariable
    ) {

        if (shiftFormula.getDayFormula().isEmpty() || shiftFormula.getDayFormula().isBlank()) {
            return "Day shift formula is not set";
        }

        KieSession kieSession = kieContainer.newKieSession();
        kieSession.insert(salaryFormulaVariable);
        kieSession.insert(shiftFormula);
        kieSession.fireAllRules();
        kieSession.dispose();
        return "Salary: " + salaryFormulaVariable.getFinalShiftSalary();
    }
}
