package com.einfochips.student.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.einfochips.student.dto.StudentDTO;
import com.einfochips.student.entity.Student;
import com.einfochips.student.service.StudentServiceImpl;
import com.einfochips.student.util.PaginationSortingUtils;

@RestController
@RequestMapping(produces = { MediaType.APPLICATION_JSON_UTF8_VALUE })
public class StudentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentController.class);

	@Autowired
	private StudentServiceImpl studentServiceImpl;

	@RequestMapping(value = "/students", method = RequestMethod.GET)
	public ResponseEntity<Page<Student>> listAllStudents(
			@RequestParam(value = "page", required = false, defaultValue = PaginationSortingUtils.PAGE) Integer page,
			@RequestParam(value = "size", required = false, defaultValue = PaginationSortingUtils.SIZE) Integer size) {
		ResponseEntity<Page<Student>> response = null;
		try {
			Pageable pageRequest = PaginationSortingUtils.getPageable(page, size);
			Page<Student> students = studentServiceImpl.findAll(pageRequest);
			if (students.getTotalElements() == 0) {
				response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				response = new ResponseEntity<>(students, HttpStatus.OK);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception in listAllBookmarks method: {}", exception);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("listAllStudents: response={} ", response);
		return response;
	}

	@RequestMapping(value = "/student/{id}", method = RequestMethod.GET)
	public ResponseEntity<Student> findStudentById(@PathVariable("id") int id) {
		ResponseEntity<Student> response = null;
		try {
			Student student = studentServiceImpl.findById(id);
			if (student == null) {
				response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				response = new ResponseEntity<>(student, HttpStatus.OK);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception in findStudentById method: {}", exception);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("findStudentById: response={} ", response);
		return response;
	}

	@RequestMapping(value = "/student", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Student> createNewStudent(@RequestBody StudentDTO studentdto) {
		ResponseEntity<Student> response = null;
		try {
			LOGGER.info("createNewStudent: request={}", studentdto);
			if (validateRequest(studentdto)) {
				Student savedStudent = studentServiceImpl.create(studentdto);
				if (savedStudent != null) {
					response = new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception in createNewStudent method: {}", exception);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("createNewStudent: response={}", response);
		return response;
	}

	@RequestMapping(value = "/student/{id}", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Student> updateStudentById(@PathVariable("id") int id, @RequestBody StudentDTO studentdto) {
		ResponseEntity<Student> response = null;
		try {
			LOGGER.info("updateStudentById: request={}", studentdto);
			if (studentdto != null) {
				Student updatedStudent = studentServiceImpl.update(id, studentdto);
				if (updatedStudent != null) {
					response = new ResponseEntity<>(updatedStudent, HttpStatus.OK);
				} else {
					response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
				}
			} else {
				response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception in createNewStudent method: {}", exception);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("updateStudentById: response={}", response);
		return response;
	}

	@RequestMapping(value = "/student/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Student> deleteStudentById(@PathVariable("id") int id) {
		ResponseEntity<Student> response = null;
		try {
			Student student = studentServiceImpl.findById(id);
			if (student == null) {
				response = new ResponseEntity<>(HttpStatus.NO_CONTENT);
			} else {
				studentServiceImpl.delete(id);
				response = new ResponseEntity<>(HttpStatus.OK);
			}
		} catch (Exception exception) {
			LOGGER.error("Exception in findStudentById method: {}", exception);
			response = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		LOGGER.info("deleteStudentById: response={}", response);
		return response;
	}

	public boolean validateRequest(StudentDTO studentDTO) {
		if (studentDTO.getFirstName() != null && studentDTO.getLastName() != null && studentDTO.getEmail() != null
				&& studentDTO.getPhoneNumber() != null)
			return true;
		else {
			return false;
		}
	}

}
