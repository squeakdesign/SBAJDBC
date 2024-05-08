package sba.sms.services;

import lombok.extern.java.Log;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import sba.sms.dao.StudentI;
import sba.sms.models.Course;
import sba.sms.models.Student;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * StudentService is a concrete class. This class implements the
 * StudentI interface, overrides all abstract service methods and
 * provides implementation for each method. Lombok @Log used to
 * generate a logger file.
 */

public class StudentService implements StudentI {

    @Override
    public List<Student> getAllStudents() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Student> allStudents = s.createQuery("from Student", Student.class).list();
        s.close();
        return allStudents;
    }

    @Override
    public void createStudent(Student student) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = s.beginTransaction();
            s.persist(student);
            transaction.commit();
        } catch (HibernateException he) {
            if (transaction != null) transaction.rollback();
            he.printStackTrace();
        } finally {
            s.close();
        }
    }

    @Override
    public Student getStudentByEmail(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Student studentByEmail = s.get(Student.class, email);
            if (studentByEmail == null) throw new HibernateException("Student not found");
            else return studentByEmail;
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            s.close();
        }
        return new Student();
    }

    @Override
    public boolean validateStudent(String email, String password) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Student student = s.get(Student.class, email);
            if (student == null) throw new HibernateException("Student not found");
            else if (student.getPassword().equals(password)) return true;
            else return false;
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            s.close();
        }
        return false;
    }

    @Override
    public void registerStudentToCourse(String email, int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = s.beginTransaction();
            Student student = getStudentByEmail(email);
            student.addCourse(s.get(Course.class, courseId));
            s.merge(student);
            transaction.commit();
        } catch (HibernateException he) {
            he.printStackTrace();
            if (transaction != null) transaction.rollback();
        } finally {
            s.close();
        }
    }

    @Override
    public List<Course> getStudentCourses(String email) {
        Session s = HibernateUtil.getSessionFactory().openSession();

        List<Course> studentCourses = s.createNativeQuery("SELECT course.id, course.name, course.instructor \n" +
                        "FROM course\n" +
                        "INNER JOIN student_courses ON course.id = student_courses.courses_id\n" +
                        "INNER JOIN student ON student_courses.student_email = student.email\n" +
                        "WHERE student.email = '" + email + "'")
                .addEntity(Course.class)
                .list();
        s.close();
        return studentCourses;
    }
}