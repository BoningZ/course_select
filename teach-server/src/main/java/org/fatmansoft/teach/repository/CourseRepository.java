package org.fatmansoft.teach.repository;

import org.fatmansoft.teach.models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course,Integer> {
    Optional<Course> findByCourseNum(String courseNum);
    Optional<Course> findByCourseName(String courseName);
    @Query(value = "select * from course_xlj  where ?1='' or num like %?1% or name like %?1% ", nativeQuery = true)
    List<Course> findCourseListByNumName(String numName);
    List<Course> findCoursesByCourseNum(String courseNum);
    List<Course> findCoursesByCourseName(String courseName);
    Boolean existsByCourseNum(String courseNum);

}
