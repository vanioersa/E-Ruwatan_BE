package ERuwatan.Tugasbe.Excell;

import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PiketRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

@Service
public class ExcellService {

    @Autowired
    PiketRepo dataRepository;

    public ByteArrayInputStream load() {
        List<Piket> data = dataRepository.findAll();
        ByteArrayInputStream in = DataExcell.dataToExcel(data);
        return in;
    }

//    @Transactional(readOnly = true)
//    public ByteArrayInputStream download(Long id) {
//        Pasien periksa = pasienRepository.findById(id).orElseThrow(() -> new NotFoundException("Id not found"));
//        StatusPeriksa statusPeriksaList = statusPeriksaRepository.findByPasien(periksa.getId());
//        ByteArrayInputStream in = DataExcell.download(periksa , statusPeriksaList);
//        return in;
//    }

    public void saveData(MultipartFile file) {
        try {
            List<Piket> dataList = DataExcell.exceltoData(file.getInputStream());
            dataRepository.saveAll(dataList);
        } catch (IOException e) {
            throw new RuntimeException("fail to store excel data: " + e.getMessage());
        }
    }

}