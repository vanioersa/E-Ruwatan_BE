package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.service.SiswaSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/siswa")
public class SiswaCont {
    @Autowired
    private SiswaSer siswaSer;

    @GetMapping("/by-kelas-id/{kelasId}")
    public ResponseEntity<List<Siswa>> getSiswaByKelasId(@PathVariable Long kelasId) {
        List<Siswa> siswa = siswaSer.getSiswaByKelasId(kelasId);
        return ResponseEntity.ok(siswa);
    }

    @PostMapping("/add")
    public SiswaDTO createMurid(@RequestBody SiswaDTO muridDTO) {
        return siswaSer.createSiswa(muridDTO);
    }

    @GetMapping("/by-id/{id}")
    public SiswaDTO getMuridById(@PathVariable Long id) {
        return siswaSer.getSiswaById(id);
    }

    @GetMapping("/all")
    public List<SiswaDTO> getAllMurids() {
        return siswaSer.getAllSiswas();
    }

    @PutMapping("/ubah/{id}")
    public SiswaDTO updateMurid(@PathVariable Long id, @RequestBody SiswaDTO siswaDTO) {
        return siswaSer.updateSiswa(id, siswaDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deleteMurid(@PathVariable Long id) {
        siswaSer.deleteSiswa(id);
    }
}