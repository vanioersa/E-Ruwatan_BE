package ERuwatan.Tugasbe.model;

import ERuwatan.Tugasbe.dto.DpiketDTO;
import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "Piket")
public class Piket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;

    private String tanggal;

    private String status;

    @ManyToMany
    private List<Siswa> siswaList = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    public String  getTanggal() {
        return tanggal;
    }

    public void setTanggal(String  tanggal) {
        this.tanggal = tanggal;
    }

    public SiswaDTO getSiswaId() {
        return null;
    }

    public KelasDTO getKelasId() {
        return null;
    }

    public void setSiswaId(SiswaDTO siswaDTO) {
    }

    public void setKelasId(KelasDTO kelasDTO) {
    }

    public void setDpiketDTOList(List<DpiketDTO> dpiketDTOS) {
    }

    public void setSiswaList(List<Siswa> siswaList) {
    }

    public Collection<Object> getSiswaList() {
        return getSiswaList();
    }
}