package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;
import ERuwatan.Tugasbe.dto.SiswaStatusDTO;
import ERuwatan.Tugasbe.model.Piket;
import ERuwatan.Tugasbe.model.Kelas;
import ERuwatan.Tugasbe.model.PiketSiswaStatus;
import ERuwatan.Tugasbe.model.Siswa;
import ERuwatan.Tugasbe.repository.PiketRepo;
import ERuwatan.Tugasbe.repository.KelasRepo;
import ERuwatan.Tugasbe.repository.PiketSiswaStatusRepo;
import ERuwatan.Tugasbe.repository.SiswaRepo;
import ERuwatan.Tugasbe.service.PiketSer;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private PiketSiswaStatusRepo piketSiswaStatusRepo;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    @Override
    public PiketDTO createPiket(PiketDTO piketDTO) {
        try {
            System.out.println("Mulai membuat Piket");
            Piket piket = new Piket();
            BeanUtils.copyProperties(piketDTO, piket);
            System.out.println("Piket: " + piket);

            if (piketDTO.getKelasId() == null) {
                throw new IllegalArgumentException("Kelas ID tidak boleh null");
            }

            Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
            if (kelasOptional.isPresent()) {
                piket.setKelas(kelasOptional.get());
            } else {
                throw new EntityNotFoundException("Kelas dengan ID " + piketDTO.getKelasId() + " tidak ditemukan");
            }

            if (piketDTO.getSiswaStatusList() == null || piketDTO.getSiswaStatusList().isEmpty()) {
                throw new IllegalArgumentException("Siswa status list tidak boleh null atau kosong");
            }

            List<PiketSiswaStatus> siswaStatusList = new ArrayList<>();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                if (siswaStatusDTO.getSiswaId() == null) {
                    throw new IllegalArgumentException("Siswa ID tidak boleh null");
                }
                Optional<Siswa> siswaOptional = siswaRepo.findById(siswaStatusDTO.getSiswaId());
                if (siswaOptional.isPresent()) {
                    PiketSiswaStatus piketSiswaStatus = new PiketSiswaStatus();
                    piketSiswaStatus.setPiket(piket);
                    piketSiswaStatus.setSiswa(siswaOptional.get());
                    piketSiswaStatus.setStatus(String.join(", ", siswaStatusDTO.getStatusList()));
                    System.out.println("PiketSiswaStatus: " + piketSiswaStatus);
                    siswaStatusList.add(piketSiswaStatus);
                } else {
                    throw new EntityNotFoundException("Siswa dengan ID " + siswaStatusDTO.getSiswaId() + " tidak ditemukan");
                }
            }
            piket.setSiswaStatus(siswaStatusList);

            try {
                piket.setTanggal(dateFormat.parse(dateFormat.format(piketDTO.getTanggal())));
            } catch (ParseException e) {
                throw new RuntimeException("Format tanggal tidak valid: " + piketDTO.getTanggal(), e);
            }

            System.out.println("Menyimpan Piket ke database");
            Piket savedPiket = piketRepo.save(piket);
            System.out.println("Piket berhasil disimpan dengan ID: " + savedPiket.getId());

            return convertToDTO(savedPiket);
        } catch (EntityNotFoundException e) {
            System.out.println("EntityNotFoundException: " + e.getMessage());
            throw new EntityNotFoundException("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
            throw new RuntimeException("Terjadi kesalahan: " + e.getMessage(), e);
        }
    }

    @Override
    public PiketDTO updatePiket(Long id, PiketDTO piketDTO) {
        try {
            Optional<Piket> piketOptional = piketRepo.findById(id);
            if (!piketOptional.isPresent()) {
                throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
            }

            Piket existingPiket = piketOptional.get();
            BeanUtils.copyProperties(piketDTO, existingPiket, "id", "siswaStatus");

            if (piketDTO.getKelasId() == null) {
                throw new IllegalArgumentException("Kelas ID tidak boleh null");
            }

            Optional<Kelas> kelasOptional = kelasRepo.findById(piketDTO.getKelasId());
            if (kelasOptional.isPresent()) {
                existingPiket.setKelas(kelasOptional.get());
            } else {
                throw new EntityNotFoundException("Kelas dengan ID " + piketDTO.getKelasId() + " tidak ditemukan");
            }

            if (piketDTO.getSiswaStatusList() == null || piketDTO.getSiswaStatusList().isEmpty()) {
                throw new IllegalArgumentException("Siswa status list tidak boleh null atau kosong");
            }

            existingPiket.getSiswaStatus().clear();
            for (SiswaStatusDTO siswaStatusDTO : piketDTO.getSiswaStatusList()) {
                if (siswaStatusDTO.getSiswaId() == null) {
                    throw new IllegalArgumentException("Siswa ID tidak boleh null");
                }
                Optional<Siswa> siswaOptional = siswaRepo.findById(siswaStatusDTO.getSiswaId());
                if (siswaOptional.isPresent()) {
                    PiketSiswaStatus piketSiswaStatus = new PiketSiswaStatus();
                    piketSiswaStatus.setPiket(existingPiket);
                    piketSiswaStatus.setSiswa(siswaOptional.get());
                    piketSiswaStatus.setStatus(String.join(", ", siswaStatusDTO.getStatusList()));
                    existingPiket.getSiswaStatus().add(piketSiswaStatus);
                } else {
                    throw new EntityNotFoundException("Siswa dengan ID " + siswaStatusDTO.getSiswaId() + " tidak ditemukan");
                }
            }

            try {
                existingPiket.setTanggal(dateFormat.parse(dateFormat.format(piketDTO.getTanggal())));
            } catch (ParseException e) {
                throw new RuntimeException("Format tanggal tidak valid: " + piketDTO.getTanggal(), e);
            }

            Piket savedPiket = piketRepo.save(existingPiket);
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
        for (PiketSiswaStatus piketSiswaStatus : piket.getSiswaStatus()) {
            SiswaStatusDTO siswaStatusDTO = new SiswaStatusDTO();
            siswaStatusDTO.setSiswaId(piketSiswaStatus.getSiswa().getId());
            siswaStatusDTO.setStatusList(Arrays.asList(piketSiswaStatus.getStatus().split(", ")));
            siswaStatusDTOList.add(siswaStatusDTO);
        }
        piketDTO.setSiswaStatusList(siswaStatusDTOList);

        try {
            piketDTO.setTanggal(dateFormat.parse(dateFormat.format(piket.getTanggal())));
        } catch (ParseException e) {
            throw new RuntimeException("Format tanggal tidak valid: " + piket.getTanggal(), e);
        }

        return piketDTO;
    }

    @Override
    public PiketDTO getPiketById(Long id) {
        Optional<Piket> piketOptional = piketRepo.findById(id);
        if (!piketOptional.isPresent()) {
            throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
        }
        return convertToDTO(piketOptional.get());
    }


    @Override
    public boolean deletePiket(Long id) {
        try {
            if (piketRepo.existsById(id)) {
                piketRepo.deleteById(id);
                return true;
            } else {
                throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
            }
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan: " + e.getMessage(), e);
        }
    }

    @Override
    public SiswaDTO getSiswaById(Long id) {
        Optional<Siswa> siswaOptional = siswaRepo.findById(id);
        if (siswaOptional.isPresent()) {
            SiswaDTO siswaDTO = new SiswaDTO();
            BeanUtils.copyProperties(siswaOptional.get(), siswaDTO);
            return siswaDTO;
        } else {
            throw new EntityNotFoundException("Siswa dengan ID " + id + " tidak ditemukan");
        }
    }

    @Override
    public KelasDTO getKelasById(Long id) {
        Optional<Kelas> kelasOptional = kelasRepo.findById(id);
        if (kelasOptional.isPresent()) {
            KelasDTO kelasDTO = new KelasDTO();
            BeanUtils.copyProperties(kelasOptional.get(), kelasDTO);
            return kelasDTO;
        } else {
            throw new EntityNotFoundException("Kelas dengan ID " + id + " tidak ditemukan");
        }
    }

    @Override
    public List<PiketDTO> getAllPiket() {
        try {
            List<Piket> piketList = piketRepo.findAll();
            return piketList.stream().map(this::convertToDTO).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Terjadi kesalahan: " + e.getMessage(), e);
        }
    }
}
