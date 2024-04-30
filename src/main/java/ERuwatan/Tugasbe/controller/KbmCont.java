package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.dto.KbmDTO;
import ERuwatan.Tugasbe.service.KbmSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kbm")
public class KbmCont {

    @Autowired
    private KbmSer kbmSer;

    @PostMapping("/add")
    public KbmDTO createKbm(@RequestBody KbmDTO kbmDTO) {
        return kbmSer.createKbm(kbmDTO);
    }

    @GetMapping("/by-id/{id}")
    public KbmDTO getKbmById(@PathVariable Long id) {
        return kbmSer.getKbmById(id);
    }

    @GetMapping("/all")
    public List<KbmDTO> getAllKbm() {
        return kbmSer.getAllKbm();
    }

    @PutMapping("/ubah/{id}")
    public KbmDTO updateKbm(@PathVariable Long id, @RequestBody KbmDTO kbmDTO) {
        return kbmSer.updateKbm(id, kbmDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public void deletKbm(@PathVariable Long id) {
        kbmSer.deletKbm(id);
    }
}