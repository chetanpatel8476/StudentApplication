package com.einfochips.rest.controller.test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.File;
import java.io.FileReader;

import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import com.einfochips.student.StudentServiceApplication;
import com.einfochips.student.config.PersistanceContext;
import com.einfochips.student.service.StudentServiceImpl;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { StudentServiceApplication.class })
@ContextConfiguration(classes = { PersistanceContext.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
		TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@TestPropertySource(locations = "classpath:application-test.properties")
public class StudentControllerTest {

	private static final Logger LOGGER = LoggerFactory.getLogger(StudentControllerTest.class);
	protected MockMvc mvc;

	@Autowired
	protected WebApplicationContext webApplicationContext;

	@Autowired
	ResourceLoader resourceLoader;

	@InjectMocks
	@Autowired
	private StudentServiceImpl studentServiceImpl;

	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void listAllStudentsIntegresionTest() throws Exception {
		String uri = "/students";
		mvc.perform(get(uri).accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())

				.andExpect(status().isOk())

				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andExpect(jsonPath("$.content", hasSize(2)))

				.andExpect(jsonPath("$.totalElements", is(equalTo(2))));
	}

	@Test
	@DatabaseSetup("/xmlFiles/no_data.xml")
	public void listAllStudentsIntegresionTest_NoContent() throws Exception {
		String uri = "/students";
		mvc.perform(get(uri).accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())

				.andExpect(status().isNoContent());
	}

	@Test
	@DatabaseSetup("/xmlFiles/no_data.xml")
	public void listAllStudentsIntegresionTest_NotFound() throws Exception {
		String uri = "/studentss";
		mvc.perform(get(uri).accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())

				.andExpect(status().isNotFound());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void findStudentByIdIntegresionTest() throws Exception {
		int studentId = 1;
		String uri = "/student/" + studentId;
		mvc.perform(get(uri))

				.andDo(print())

				.andExpect(status().isOk())

				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andExpect(jsonPath("$.id", is(equalTo(1))));
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void findStudentByIdIntegresionTest_NoContent() throws Exception {
		int studentId = 10;
		String uri = "/student/" + studentId;
		mvc.perform(get(uri))

				.andDo(print())

				.andExpect(status().isNoContent());
	}

	@Test
	@DatabaseSetup("/xmlFiles/no_data.xml")
	public void findStudentByIdIntegresionTest_NotFound() throws Exception {
		int studentId = 1;
		String uri = "/students/" + studentId;
		mvc.perform(get(uri).accept(MediaType.APPLICATION_JSON_UTF8_VALUE)).andDo(print())

				.andExpect(status().isNotFound());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void createNewStudentIntegresionTest() throws Exception {
		String uri = "/student";
		String fileName = "/Student/addNewUser.json";
		String inputJson = readJsonFromFile(fileName).toString();
		mvc.perform(post(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andDo(print())

				.andExpect(status().isCreated()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andExpect(jsonPath("$.firstName", is(equalTo("test"))))

				.andExpect(jsonPath("$.lastName", is(equalTo("test1"))))

				.andExpect(jsonPath("$.phoneNumber", is(equalTo(1234567893))))

				.andExpect(jsonPath("$.email", is(equalTo("test@gmail.com"))));
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void createNewStudentIntegresionTest_UnsupportedMediaType() throws Exception {
		String uri = "/student";
		String fileName = "/Student/addNewUser.json";
		String inputJson = readJsonFromFile(fileName).toString();
		mvc.perform(post(uri).content(inputJson))

				.andDo(print())

				.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void createNewStudentIntegresionTest_BadRequest() throws Exception {
		String uri = "/student/";
		String inputJson = "/Student/invalid.json";
		mvc.perform(post(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andDo(print())

				.andExpect(status().isBadRequest());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void updateStudentByIdIntegresionTest() throws Exception {
		int studentId = 1;
		String uri = "/student/" + studentId;
		String fileName = "/Student/updateUser.json";
		String inputJson = readJsonFromFile(fileName).toString();
		mvc.perform(put(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andDo(print())

				.andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andExpect(jsonPath("$.firstName", is(equalTo("test"))))

				.andExpect(jsonPath("$.lastName", is(equalTo("test1"))))

				.andExpect(jsonPath("$.phoneNumber", is(equalTo(4567890123L))))

				.andExpect(jsonPath("$.email", is(equalTo("test@gmail.com"))));
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void updateStudentByIdIntegresionTest_UnsupportedMediaType() throws Exception {
		int studentId = 1;
		String uri = "/student/" + studentId;
		String fileName = "/Student/updateUser.json";
		String inputJson = readJsonFromFile(fileName).toString();
		mvc.perform(put(uri).content(inputJson))

				.andDo(print())

				.andExpect(status().isUnsupportedMediaType());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void updateStudentByIdIntegresionTest_NoContent() throws Exception {
		int studentId = 10;
		String uri = "/student/" + studentId;
		String fileName = "/Student/updateUser.json";
		String inputJson = readJsonFromFile(fileName).toString();
		mvc.perform(put(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andDo(print())

				.andExpect(status().isNoContent());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void updateStudentByIdIntegresionTest_BadRequest() throws Exception {
		int studentId = 1;
		String uri = "/student/" + studentId;
		String inputJson = "";
		mvc.perform(put(uri).content(inputJson).contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))

				.andDo(print())

				.andExpect(status().isBadRequest());
	}

	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void deleteStudentByIdIntegresionTest() throws Exception {
		int studentId = 1;
		String uri = "/student/" + studentId;
		mvc.perform(delete(uri))

				.andDo(print())

				.andExpect(status().isOk());
	}
	
	@Test
	@DatabaseSetup("/xmlFiles/student.xml")
	public void deleteStudentByIdIntegresionTest_NoContent() throws Exception {
		int studentId = 10;
		String uri = "/student/" + studentId;
		mvc.perform(delete(uri))

				.andDo(print())

				.andExpect(status().isNoContent());
	}

	protected JSONObject readJsonFromFile(String fileName) {
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = null;
		try {
			Resource resource = resourceLoader.getResource("classpath:" + fileName);
			File file = resource.getFile();
			Object obj = parser.parse(new FileReader(file));
			jsonObject = new JSONObject(obj.toString());
		} catch (Exception e) {
			LOGGER.error(">>>> ERROR ");
			e.printStackTrace();
		}
		return jsonObject;

	}

}
