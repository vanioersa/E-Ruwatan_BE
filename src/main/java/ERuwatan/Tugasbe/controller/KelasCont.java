package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.service.KelasSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kelas")
public class KelasCont {
    @Autowired
    private KelasSer kelasSer;

    @PostMapping("/add")
    public KelasDTO createKelas(@RequestBody KelasDTO kelasDTO) {
        return kelasSer.createKelas(kelasDTO);
    }

    @GetMapping("/by-id/{id}")
    public KelasDTO getKelasById(@PathVariable Long id) {
        return kelasSer.getKelasById(id);
    }

    @GetMapping("/all")
    public List<KelasDTO> getAllKelass() {
        return kelasSer.getAllKelas();
    }

    @PutMapping("/ubah/{id}")
    public KelasDTO updateKelas(@PathVariable Long id, @RequestBody KelasDTO kelasDTO) {
        return kelasSer.updateKelas(id, kelasDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deleteKelas(@PathVariable Long id) {
        kelasSer.deleteKelas(id);
    }
}
