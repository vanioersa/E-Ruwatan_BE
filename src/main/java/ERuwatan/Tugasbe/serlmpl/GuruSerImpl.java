//package ERuwatan.Tugasbe.serlmpl;
//
//import ERuwatan.Tugasbe.dto.GuruDTO;
//import ERuwatan.Tugasbe.model.Guru;
//import ERuwatan.Tugasbe.model.Kelas;
//import ERuwatan.Tugasbe.repository.GuruRepo;
//import ERuwatan.Tugasbe.repository.KelasRepo;
//import ERuwatan.Tugasbe.service.GuruSer;
//import org.springframework.beans.BeanUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//@Service
//public class GuruSerImpl implements GuruSer {
//
//    @Autowired
//    private GuruRepo guruRepo;
//
//    @Autowired
//    private KelasRepo kelasRepo;
//
//    @Override
//    public GuruDTO createGuru(GuruDTO guruDTO) {
//        Guru guru = new Guru();
//        BeanUtils.copyProperties(guruDTO, guru);
//        Optional<Kelas> kelasOptional = kelasRepo.findById(guruDTO.getKelasId());
//        kelasOptional.ifPresent(guru::setKelas);
//        return convertToDTO(guruRepo.save(guru));
//    }
//
//    @Override
//    public GuruDTO getGuruById(Long id) {
//        Optional<Guru> guruOptional = guruRepo.findById(id);
//        return guruOptional.map(this::convertToDTO).orElse(null);
//    }
//
//    @Override
//    public List<GuruDTO> getAllGurus() {
//        return guruRepo.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
//    }
//
//    @Override
//    public GuruDTO updateGuru(Long id, GuruDTO guruDTO) {
//        Optional<Guru> optionalGuru = guruRepo.findById(id);
//        if (optionalGuru.isPresent()) {
//            Guru guru = optionalGuru.get();
//            BeanUtils.copyProperties(guruDTO, guru);
//            Optional<Kelas> kelasOptional = kelasRepo.findById(guruDTO.getKelasId());
//            kelasOptional.ifPresent(guru::setKelas);
//            guru.setId(id);
//            return convertToDTO(guruRepo.save(guru));
//        }
//        return null;
//    }
//
//    @Override
//    public void deleteGuru(Long id) {
//        guruRepo.deleteById(id);
//    }
//
//    private GuruDTO convertToDTO(Guru guru) {
//        GuruDTO guruDTO = new GuruDTO();
//        BeanUtils.copyProperties(guru, guruDTO);
//        guruDTO.setKelasId(guru.getKelas() != null ? guru.getKelas().getId() : null);
//        return guruDTO;
//    }
//}
