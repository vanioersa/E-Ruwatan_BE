//package ERuwatan.Tugasbe.controller;
//
//import ERuwatan.Tugasbe.Excell.ExcelUserSer;
//import ERuwatan.Tugasbe.dto.GuruDTO;
//import ERuwatan.Tugasbe.service.GuruSer;
//import javassist.NotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.List;
//
//@RestController
//@RequestMapping("/guru")
//public class GuruCont {
//    @Autowired
//    private GuruSer guruSer;
//
//    @Autowired
//    private ExcelUserSer excelGuruSer;
//
//    @PostMapping("/add")
//    public GuruDTO createGuru(@RequestBody GuruDTO guruDTO) {
//        return guruSer.createGuru(guruDTO);
//    }
//
//    @GetMapping("/by-id/{id}")
//    public GuruDTO getGuruById(@PathVariable Long id) {
//        return guruSer.getGuruById(id);
//    }
//
//    @GetMapping("/all")
//    public List<GuruDTO> getAllGurus() {
//        return guruSer.getAllGurus();
//    }
//
//    @PutMapping("/ubah/{id}")
//    public GuruDTO updateGuru(@PathVariable Long id, @RequestBody GuruDTO guruDTO) {
//        return guruSer.updateGuru(id, guruDTO);
//    }
//
//    @DeleteMapping("/hapus/{id}")
//    public void deleteGuru(@PathVariable Long id) {
//        guruSer.deleteGuru(id);
//    }
//
//    @GetMapping("/upload/export-guru")
//    public void exportGuru(
//            HttpServletResponse response) throws IOException, NotFoundException {
//        excelGuruSer.excelExportGuru(response);
//    }
//
//    @GetMapping("/download/template-guru")
//    public void excelDownloadGuruTemplate(HttpServletResponse response) throws IOException {
//        excelGuruSer.excelDownloadGuruTemplate(response);
//    }
//
//    @PostMapping("/upload/importGuru")
//    public ResponseEntity<String> importSiswaFromExcel(@RequestPart("file") MultipartFile file) {
//        try {
//            excelGuruSer.importGuruFromExcel(file);
//            return new ResponseEntity<>("Import successful", HttpStatus.OK);
//        } catch (IOException e) {
//            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        } catch (IllegalArgumentException e) {
//            return new ResponseEntity<>("Import failed: " + e.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//}