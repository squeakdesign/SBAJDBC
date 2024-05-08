package sba.sms.services;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import sba.sms.dao.CourseI;
import sba.sms.models.Course;
import sba.sms.utils.HibernateUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * CourseService is a concrete class. This class implements the
 * CourseI interface, overrides all abstract service methods and
 * provides implementation for each method.
 */
public class CourseService implements CourseI {

    @Override
    public void createCourse(Course course) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = s.beginTransaction();
            s.persist(course);
            transaction.commit();

        } catch (HibernateException he) {
            if (transaction != null) transaction.rollback();
            he.printStackTrace();
        } finally {
            s.close();
        }
    }

    @Override
    public Course getCourseById(int courseId) {
        Session s = HibernateUtil.getSessionFactory().openSession();
        try {
            Course courseById = s.get(Course.class, courseId);
            if (courseById == null) throw new HibernateException("Course not found");
            else return courseById;
        } catch (HibernateException he) {
            he.printStackTrace();
        } finally {
            s.close();
        }
        return new Course();
    }

    @Override
    public List<Course> getAllCourses() {
        Session s = HibernateUtil.getSessionFactory().openSession();
        List<Course> allCourses = s.createQuery("from Course", Course.class).list();
        s.close();
        return allCourses;
    }
}