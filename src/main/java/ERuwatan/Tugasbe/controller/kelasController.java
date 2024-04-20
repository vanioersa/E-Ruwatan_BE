package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.service.kelasService;
import ERuwatan.Tugasbe.dto.kelasDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kelas")
@CrossOrigin(origins = "*")
public class kelasController {
    @Autowired
    private kelasService kelasService;

    @GetMapping
    public ResponseEntity<List<kelasDTO>> getAllData() {
        List<kelasDTO> kelasDTOS = kelasService.getAllKelas();
        return new ResponseEntity<>(kelasDTOS, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<kelasDTO> createData(@RequestBody kelasDTO kelasDTO) {
        kelasDTO newData = kelasService.createKelas(kelasDTO);
        return new ResponseEntity<>(newData, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        kelasService.deleteKelas(id);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
