package sba.sms.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;


/**
 * Student is a POJO, configured as a persistent class that represents (or maps to) a table
 * name 'student' in the database. A Student object contains fields that represent student
 * login credentials and a join table containing a registered student's email and course(s)
 * data. The Student class can be viewed as the owner of the bi-directional relationship.
 * Implement Lombok annotations to eliminate boilerplate code.
 */
@Table(name = "student")
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@Entity
public class Student {
    @Id
    @NonNull
    @Column(length = 50, unique = true, nullable = false, name = "email")
    String email;

    @NonNull
    @Column(length = 50, nullable = false, name = "name")
    String name;

    @NonNull
    @Column(length = 50, nullable = false, name = "password")
    String password;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.EAGER)
    @JoinTable(name = "student_courses",
            joinColumns = @JoinColumn(name = "student_email"),
            inverseJoinColumns = @JoinColumn(name = "courses_id")
    )
    //ArrayList<Course> courses = new ArrayList<>();
    Set<Course> courses = new LinkedHashSet<>();

    public void addCourse(Course course){
        courses.add(course);
        course.getStudents().add(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return email.equals(student.email) && name.equals(student.name) && password.equals(student.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, name, password);
    }
}





