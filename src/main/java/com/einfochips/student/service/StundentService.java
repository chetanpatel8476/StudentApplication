package com.einfochips.student.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.einfochips.student.dto.StudentDTO;
import com.einfochips.student.entity.Student;

public interface StundentService {

	public Student findById(Integer id);

	public Student create(StudentDTO studentdto);

	public Student update(Integer id, StudentDTO studentdto);

	public void delete(Integer id);

	public Page<Student> findAll(Pageable pageRequest);

}
