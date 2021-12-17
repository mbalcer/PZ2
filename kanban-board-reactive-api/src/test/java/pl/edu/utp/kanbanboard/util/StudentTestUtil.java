package pl.edu.utp.kanbanboard.util;

import pl.edu.utp.kanbanboard.model.Student;
import reactor.core.publisher.Flux;

import java.util.UUID;

public class StudentTestUtil {
    public static Student getStudent1() {
        return new Student(UUID.randomUUID().toString(),
                "Jan",
                "Kowalski",
                "111000",
                true,
                "jankow@wp.pl",
                "111222");
    }

    public static Student getStudent2() {
        return new Student(UUID.randomUUID().toString(),
                "Andrzej",
                "Nowak",
                "123321",
                true,
                "andnow@wp.pl",
                "123456");
    }

    public static Student getStudent3() {
        return new Student(UUID.randomUUID().toString(),
                "Michał",
                "Daniel",
                "122000",
                false,
                "micdan@wp.pl",
                "michal123!");
    }

    public static Flux<Student> getStudentsFlux() {
        return Flux.just(getStudent1(), getStudent2(), getStudent3());
    }
}
