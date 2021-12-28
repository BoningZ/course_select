package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.fatmansoft.teach.models.Select;
import org.fatmansoft.teach.models.Student;
import org.fatmansoft.teach.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SelectRepository extends JpaRepository<Select,Integer> {
    List<Select> getSelectsByStudent(Student student);
    List<Select> getSelectsByCourse(Course course);
    Optional<Select> getSelectsByStudentAndCourse(Student student,Course course);
}
