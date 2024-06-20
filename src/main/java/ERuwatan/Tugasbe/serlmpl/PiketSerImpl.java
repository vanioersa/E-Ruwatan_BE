package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.dto.SiswaStatusDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.PiketRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class PiketSerImpl implements PiketSer {
    @Autowired
    private PiketRepo piketRepo;
    @Autowired
    private KelasRepo kelasRepo;
    @Autowired
    private SiswaRepo siswaRepo;

    @Override
    public PiketDTO createPiket(PiketDTO piketDTO) {
        try {
            Piket piket = new Piket();
            BeanUtils.copyProperties(piketDTO, piket);

            Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
            if (kelasOptional.isPresent()) {
                piket.setKelas(kelasOptional.get());
            } else {
                throw new EntityNotFoundException("Kelas dengan ID " + piketDTO.getKelasId() + " tidak ditemukan");
            }

            List<Siswa> siswaList = new ArrayList<>();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                Optional<Siswa> siswaOptional = siswaRepo.findById(siswaStatusDTO.getSiswaId());
                if (siswaOptional.isPresent()) {
                    siswaList.add(siswaOptional.get());
                } else {
                    throw new EntityNotFoundException("Siswa dengan ID " + siswaStatusDTO.getSiswaId() + " tidak ditemukan");
                }
            }
            piket.setSiswa(siswaList); // Set list siswa ke piket

            // Mengonversi status siswa menjadi List<String>
            List<String> statusList = new ArrayList<>();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                statusList.addAll(siswaStatusDTO.getStatusList());
            }
            piket.setStatus(statusList);
            piket.setTanggal(piketDTO.getTanggal()); // Set tanggal

            Piket savedPiket = piketRepo.save(piket);
            return convertToDTO(savedPiket);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan: " + e.getMessage(), e);
        }
    }

    private PiketDTO convertToDTO(Piket piket) {
        PiketDTO piketDTO = new PiketDTO();
        BeanUtils.copyProperties(piket, piketDTO);
        piketDTO.setId(piket.getId());
        piketDTO.setKelasId(piket.getKelas() != null ? piket.getKelas().getId() : null);

        List<SiswaStatusDTO> siswaStatusDTOList = new ArrayList<>();
        for (Siswa siswa : piket.getSiswa()) {
            SiswaStatusDTO siswaStatusDTO = new SiswaStatusDTO();
            siswaStatusDTO.setSiswaId(siswa.getId());
            siswaStatusDTO.setStatusList(piket.getStatus());
            siswaStatusDTOList.add(siswaStatusDTO);
        }
        piketDTO.setSiswaStatusList(siswaStatusDTOList);
        return piketDTO;
    }

    @Override
    public PiketDTO getPiketById(Long id) {
        Optional<Piket> piketOptional = piketRepo.findById(id);
        return piketOptional.map(this::convertToDTO).orElse(null);
    }

    @Override
    public PiketDTO updatePiket(Long id, PiketDTO piketDTO) {
        try {
            Optional<Piket> piketOptional = piketRepo.findById(id);
            if (!piketOptional.isPresent()) {
                throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
            }

            Piket existingPiket = piketOptional.get();
            // Perbarui properti Piket dengan data dari PiketDTO
            BeanUtils.copyProperties(piketDTO, existingPiket, "id", "siswa", "status");

            Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
            kelasOptional.ifPresent(existingPiket::setKelas);

            List<Siswa> siswaList = new ArrayList<>();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                Optional<Siswa> siswaOptional = siswaRepo.findById(siswaStatusDTO.getSiswaId());
                if (siswaOptional.isPresent()) {
                    siswaList.add(siswaOptional.get());
                } else {
                    throw new EntityNotFoundException("Siswa dengan ID " + siswaStatusDTO.getSiswaId() + " tidak ditemukan");
                }
            }
            existingPiket.setSiswa(siswaList);

            // Mengonversi status menjadi List<String>
            List<String> statusList = new ArrayList<>();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                statusList.addAll(siswaStatusDTO.getStatusList());
            }
            existingPiket.setStatus(statusList);

            existingPiket.setTanggal(piketDTO.getTanggal()); // Set tanggal

            Piket updatedPiket = piketRepo.save(existingPiket);
            return convertToDTO(updatedPiket);
        } catch (EntityNotFoundException e) {
            throw new EntityNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan: " + e.getMessage(), e);
        }
    }

    @Override
    public void deletePiket(Long id) {
        if (piketRepo.existsById(id)) {
            piketRepo.deleteById(id);
        } else {
            throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
        }
    }

    @Override
    public List<SiswaDTO> getStudentsByClass(Long kelasId) {
        return null;
    }

    @Override
    public List<PiketDTO> getPiketByKelas(Long kelasId) {
        List<Piket> piketList = piketRepo.findByKelasId(kelasId);
        return piketList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStudentStatus(List<PiketDTO> piketDTOList) {
        for (PiketDTO piketDTO : piketDTOList) {
            updatePiket(piketDTO.getId(), piketDTO);
        }
    }

    @Override
    public PiketDTO addSiswaToPiket(PiketDTO piketDTO) {
        return createPiket(piketDTO); // Gunakan createPiket untuk menambahkan siswa
    }

    @Override
    public List<PiketDTO> getPiketByDateAndClass(String tanggal, Long kelasId) {
        return null;
    }

    @Override
    public boolean deletePiketByDateAndClass(String tanggal, Long kelasId) {
        try {
            List<Piket> piketList = piketRepo.findByTanggalAndKelasId(tanggal, kelasId);
            if (piketList.isEmpty()) {
                return false;
            }
            piketRepo.deleteAll(piketList);
            return true;
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan saat menghapus piket berdasarkan tanggal dan kelas: " + e.getMessage(), e);
        }
    }

    @Override
    public List<PiketDTO> getAllPiket() {
        List<Piket> piketList = piketRepo.findAll();
        List<PiketDTO> piketDTOList = new ArrayList<>();
        for (Piket piket : piketList) {
            piketDTOList.add(convertToDTO(piket));
        }
        return piketDTOList;
    }
}
