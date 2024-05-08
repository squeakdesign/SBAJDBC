package sba.sms.services;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import sba.sms.models.Student;
import sba.sms.utils.CommandLine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;


@FieldDefaults(level = AccessLevel.PRIVATE)
class StudentServiceTest {

    static StudentService studentService;

    @BeforeAll
    static void beforeAll() {
        studentService = new StudentService();
        CommandLine.addData();
    }




    @Test
    void validateStudent(){
        Student student = studentService.getStudentByEmail("reema@gmail.com");
        assertThat(studentService.validateStudent(student.getEmail(), student.getPassword())).isEqualTo(true);
        assertThat(studentService.validateStudent(student.getEmail(), "wrong password!")).isEqualTo(false);
        assertThat(studentService.validateStudent("wrongemail@gmail.net", "password")).isEqualTo(false);
    }

}