package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.model.GuruModel;
import ERuwatan.Tugasbe.service.GuruService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guru")
public class GuruController {
    @Autowired
    private GuruService guruService;

    @GetMapping("/all")
    public ResponseEntity<List<GuruModel>> getAllGuru() {
        List<GuruModel> allGuru = guruService.getAllGuru();
        return ResponseEntity.ok(allGuru);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GuruModel> getGuruById(@PathVariable Long id) {
        try {
            GuruModel guru = guruService.getGuruById(id);
            return ResponseEntity.ok(guru);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GuruModel> createGuru(@RequestBody GuruModel guru) {
        GuruModel createdGuru = guruService.createGuru(guru);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGuru);
    }

    @PutMapping("/{id}")
    public ResponseEntity<GuruModel> updateGuru(@PathVariable Long id, @RequestBody GuruModel guru) {
        try {
            GuruModel updatedGuru = guruService.updateGuru(id, guru);
            return ResponseEntity.ok(updatedGuru);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGuru(@PathVariable Long id) {
        try {
            guruService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
