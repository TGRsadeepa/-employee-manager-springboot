package com.example.employee_manager.controller;


import com.example.employee_manager.model.Employee;
import com.example.employee_manager.service.EmployeeService;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // READ: display the registration form and employee table.
    @GetMapping
    public String showEmployees(
            @RequestParam(name = "keyword", required = false) String keyword,
            Model model) {
        model.addAttribute("employee", new Employee());
        model.addAttribute(
                "employees",
                employeeService.getAllEmployees(keyword)
        );
        model.addAttribute("keyword", keyword == null ? "" : keyword);

        return "employees";
    }

    // CREATE: validate and save a new employee.
    @PostMapping
    public String createEmployee(
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (!bindingResult.hasFieldErrors("email")
                && employeeService.emailExists(employee.getEmail())) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "This email is already registered"
            );
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute(
                    "employees",
                    employeeService.getAllEmployees(null)
            );

            return "employees";
        }

        employeeService.createEmployee(employee);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee added successfully"
        );

        return "redirect:/employees";
    }

    // EDIT: display a form containing the existing employee details.
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable("id") Long id,
            Model model) {

        model.addAttribute(
                "employee",
                employeeService.getEmployeeById(id)
        );

        return "edit-employee";
    }

    // UPDATE: validate and save changes to an existing employee.
    @PostMapping("/{id}/update")
    public String updateEmployee(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("employee") Employee employee,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {

        // Check that the employee exists and use the ID from the URL.
        employeeService.getEmployeeById(id);
        employee.setId(id);

        if (!bindingResult.hasFieldErrors("email")
                && employeeService.emailExistsForOtherEmployee(
                        employee.getEmail(), id)) {

            bindingResult.rejectValue(
                    "email",
                    "duplicate",
                    "This email is already registered"
            );
        }

        if (bindingResult.hasErrors()) {
            return "edit-employee";
        }

        employeeService.updateEmployee(id, employee);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee updated successfully"
        );

        return "redirect:/employees";
    }

    // DELETE: remove the employee.
    @PostMapping("/{id}/delete")
    public String deleteEmployee(
            @PathVariable("id") Long id,
            RedirectAttributes redirectAttributes) {

        employeeService.deleteEmployee(id);

        redirectAttributes.addFlashAttribute(
                "successMessage",
                "Employee deleted successfully"
        );

        return "redirect:/employees";
    }
    
}
