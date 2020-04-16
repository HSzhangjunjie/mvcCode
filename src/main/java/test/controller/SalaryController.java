package test.controller;


import framework.beans.AutoWired;
import test.service.SalaryService;
import framework.web.mvc.Controller;
import framework.web.mvc.RequestMapping;
import framework.web.mvc.RequestParam;

@Controller
public class SalaryController {

    @AutoWired
    private SalaryService salaryService;

    @RequestMapping("/getSalary")
    public Integer getSalary(@RequestParam("name") String name,
                             @RequestParam("experience") String a) {
        return salaryService.calSalary(Integer.parseInt(a));
    }
}
