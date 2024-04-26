package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.GuruDTO;
import ERuwatan.Tugasbe.service.GuruSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/guru")
public class GuruCont {
    @Autowired
    private GuruSer guruSer;

    @PostMapping("/add")
    public GuruDTO createGuru(@RequestBody GuruDTO guruDTO) {
        return guruSer.createGuru(guruDTO);
    }

    @GetMapping("/by-id/{id}")
    public GuruDTO getGuruById(@PathVariable Long id) {
        return guruSer.getGuruById(id);
    }

    @GetMapping("/all")
    public List<GuruDTO> getAllGurus() {
        return guruSer.getAllGurus();
    }

    @PutMapping("/ubah/{id}")
    public GuruDTO updateGuru(@PathVariable Long id, @RequestBody GuruDTO guruDTO) {
        return guruSer.updateGuru(id, guruDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deleteGuru(@PathVariable Long id) {
        guruSer.deleteGuru(id);
    }
}