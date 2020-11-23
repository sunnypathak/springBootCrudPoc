package com.example.demo.controllers;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.demo.entity.Student;
import com.example.demo.repository.StudentRepository;

@Controller
@RequestMapping("/students/")
public class StudentController {
	
	@Autowired
	private StudentRepository studentRepository;
	
	@GetMapping("showForm")
	public String getSignUpForm(Model model) {
		model.addAttribute("student", new Student());
		return "add-student";
	}
	
	@GetMapping("edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model ) {
		Student student = this.studentRepository.findById(id).
				orElseThrow(() -> new IllegalArgumentException("Invalid Student id"+ id));
		model.addAttribute("student", student);
		return "update-student";
	}
	
	@GetMapping("list")
	public String students(Model model) {
		model.addAttribute("students", this.studentRepository.findAll().size()>0?this.studentRepository.findAll():null);
		return "index";
	}
	
	@PostMapping("add")
	public String addStudent(@Validated Student student, BindingResult result,Model model ) {
		if(result.hasErrors()) {
			return "add-student";
		}
		this.studentRepository.save(student);
		return "redirect:list";
	}
	
	@PostMapping("update/{id}")
	public String updateStudent(@PathVariable("id") long id, Student student, BindingResult result, Model model) {
		if(result.hasErrors()) {
			student.setId(id);
			return "update-student";
		}
		//update student
		studentRepository.save(student);
		
		//get All Students after update
		model.addAttribute("students", this.studentRepository.findAll());
		return "index";
	}
	
	@GetMapping("delete/{id}")
	public String deleteStudent(@PathVariable("id") long id,Model model) {
		Student student = this.studentRepository.findById(id).
				orElseThrow(() -> new IllegalArgumentException("Invalid Student id"+ id));
		this.studentRepository.delete(student);
		model.addAttribute("students", this.studentRepository.findAll());
		return "index";
	}
}
