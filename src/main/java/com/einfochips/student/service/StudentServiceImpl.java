package com.einfochips.student.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.einfochips.student.dto.StudentDTO;
import com.einfochips.student.entity.Student;
import com.einfochips.student.repository.StudentRepository;

@Service
public class StudentServiceImpl implements StundentService {

	@Autowired
	private StudentRepository studentRepository;

	@Override
	public Student findById(Integer id) {
		return studentRepository.findOne(id);
	}

	@Override
	public Student create(StudentDTO studentdto) {
		Student student = new Student();
		student.setFirstName(studentdto.getFirstName());
		student.setLastName(studentdto.getLastName());
		student.setEmail(studentdto.getEmail());
		student.setPhoneNumber(studentdto.getPhoneNumber());
		return studentRepository.save(student);
	}

	@Override
	public Student update(Integer id, StudentDTO studentdto) {
		Student student = studentRepository.findOne(id);
		if (student != null) {
			student.setFirstName(studentdto.getFirstName());
			student.setLastName(studentdto.getLastName());
			student.setEmail(studentdto.getEmail());
			student.setPhoneNumber(studentdto.getPhoneNumber());

			return studentRepository.saveAndFlush(student);
		} else {
			return student;
		}
	}

	@Override
	public void delete(Integer id) {
		studentRepository.delete(id);
	}

	@Override
	public Page<Student> findAll(Pageable pageRequest) {
		return studentRepository.findAll(pageRequest);
	}

}
