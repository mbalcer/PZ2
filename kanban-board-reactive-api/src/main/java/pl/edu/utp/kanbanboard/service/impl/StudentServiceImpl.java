package pl.edu.utp.kanbanboard.service.impl;

import org.springframework.stereotype.Service;
import pl.edu.utp.kanbanboard.model.Student;
import pl.edu.utp.kanbanboard.repository.StudentRepository;
import pl.edu.utp.kanbanboard.service.StudentService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Flux<Student> all() {
        return this.studentRepository.findAll();
    }

    @Override
    public Mono<Student> get(String id) {
        return this.studentRepository.findById(id);
    }

    @Override
    public Mono<Student> getByEmail(String email) {
        return this.studentRepository.getByEmail(email);
    }

    @Override
    public Mono<Student> create(Student newStudent) {
        return Mono.just(newStudent)
                .map(student -> {
                    student.setStudentId(UUID.randomUUID().toString());
                    return student;
                })
                .flatMap(studentRepository::save);
    }

    @Override
    public Mono<Student> update(String id, Student updateStudent) {
        return this.studentRepository
                .findById(id)
                .doOnNext(student -> {
                    student.setEmail(updateStudent.getEmail());
                    student.setFirstName(updateStudent.getFirstName());
                    student.setLastName(updateStudent.getLastName());
                    student.setIndexNumber(updateStudent.getIndexNumber());
                    student.setFullTime(updateStudent.getFullTime());
                })
                .flatMap(this.studentRepository::save);
    }

    @Override
    public Mono<Student> delete(String id) {
        return this.studentRepository
                .findById(id)
                .flatMap(s -> this.studentRepository.deleteById(s.getStudentId()).thenReturn(s));
    }
}
