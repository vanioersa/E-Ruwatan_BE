package ERuwatan.Tugasbe.controller;

import ERuwatan.Tugasbe.Excell.ExcelPiket;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.service.ResponseMessage;

import javax.persistence.EntityNotFoundException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("/piket")
public class PiketCont {
    @Autowired
    private PiketSer piketSer;

    @PostMapping("/add")
    public PiketDTO createPiket(@RequestBody PiketDTO piketDTO) {
        return piketSer.createPiket(piketDTO);
    }

    @GetMapping("/by-id/{id}")
    public PiketDTO getPiketById(@PathVariable Long id) {
        return piketSer.getPiketById(id);
    }

    @GetMapping("/all")
    public List<PiketDTO> getAllPikets() {
        return piketSer.getAllPikets();
    }

    @PutMapping("/ubah/{id}")
    public PiketDTO updatePiket(@PathVariable Long id, @RequestBody PiketDTO piketDTO) {
        return piketSer.updatePiket(id, piketDTO);
    }

    @DeleteMapping("/hapus/{id}")
    public ResponseEntity<?> deletePiket(@PathVariable Long id) {
        try {
            piketSer.deletePiket(id);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Piket dengan ID " + id + " tidak ditemukan");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Gagal menghapus data: " + e.getMessage());
        }
    }

//    @PostMapping(path = "/upload/importPiket")
//    public ResponseEntity<ResponseMessage> uploadFile(@RequestPart("file") MultipartFile file) {
//        String message = "";
//        if (ExcelPiket.hasExcelFormat(file)) {
//            try {
//                ExcelPiket.excelPiket((InputStream) file);
//              message = "Uploaded the file successfully: " + file.getOriginalFilename();
//                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
//
//            } catch (Exception e) {
//                System.out.println(e);
//                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
//                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//            }
//        }
//        message = "Please upload an excel file!";
//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
//    }

}