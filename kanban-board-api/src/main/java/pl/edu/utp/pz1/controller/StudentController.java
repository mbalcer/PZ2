package pl.edu.utp.pz1.controller;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.edu.utp.pz1.model.Student;
import pl.edu.utp.pz1.service.StudentService;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/student")
@CrossOrigin(origins = "http://localhost:4200")
public class StudentController {

    private StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/{studentId}")
    public ResponseEntity<Student> getStudentById(@PathVariable Integer studentId) {
        return ResponseEntity.of(studentService.findById(studentId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Student> getStudentByEmail(@PathVariable String email) {
        return ResponseEntity.of(studentService.findByEmail(email));
    }

    @GetMapping
    public Page<Student> getStudents(@RequestParam("page") int page, @RequestParam("size") int size) {
        return studentService.getStudents(PageRequest.of(page, size));
    }

    @GetMapping("/all")
    public List<Student> findAll() {
        return studentService.findAll();
    }

    @PostMapping
    public ResponseEntity<Student> createStudent(@Valid @RequestBody Student student) {
        Student newStudent = studentService.create(student);
        if (newStudent != null) {
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/" + newStudent.getStudentId()).build().toUri();
            return ResponseEntity.created(location).body(newStudent);
        } else {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{studentId}")
    public ResponseEntity<Student> updateStudent(@PathVariable Integer studentId, @RequestBody Student student) {
        Student updatedStudent = studentService.update(studentId, student);
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/password/{studentId}")
    public ResponseEntity<Student> updatePassword(@PathVariable Integer studentId, @RequestBody ObjectNode objectNode) {
        String currentPassword = objectNode.get("currentPassword").asText();
        String newPassword = objectNode.get("newPassword").asText();
        Student updatedStudent = studentService.updatePassword(studentId, currentPassword, newPassword);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
        studentService.delete(studentId);
        return ResponseEntity.noContent().build();
    }
}
