
package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.PenilaianDTO;
import ERuwatan.Tugasbe.serlmpl.PenilaianSerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/panilaian")
public class PenilaianCont {
    @Autowired
    private PenilaianSerImpl penilaianSer;

    @PostMapping("/add")
    private PenilaianDTO createPenilaian (@RequestBody PenilaianDTO penilaianDTO) {
        return penilaianSer.createPenilaian(penilaianDTO);
    }

    @GetMapping("/by-id/{id}")
    public PenilaianDTO getPenilaianById(@PathVariable Long id) {
        return penilaianSer.getPenilaianById(id);
    }

    @GetMapping("/all")
    public List<PenilaianDTO> getAllPenilaian() {
        return penilaianSer.getAllPenilaian();
    }

    @PutMapping("/ubah/{id}")
    public PenilaianDTO updatePenilaian(@PathVariable Long id, @RequestBody PenilaianDTO penilaianDTO) {
        return penilaianSer.updatePenilaian(id, penilaianDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deletePenilaian(@PathVariable Long id) {
        penilaianSer.deletePenilaian(id);
    }

    @PostMapping("/import")
    public void importPenilaianData(@RequestParam("file") MultipartFile file) throws IOException {
        penilaianSer.importData(file);
    }
}
