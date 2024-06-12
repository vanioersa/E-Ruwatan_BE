package ERuwatan.Tugasbe.model;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.SiswaDTO;

import javax.persistence.*;
import java.util.ArrayList;
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

    @OneToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    private String tanggal;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Kelas getKelas() {
        return kelas;
    }

    public void setKelas(Kelas kelas) {
        this.kelas = kelas;
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

    public double getStatus() {
        return getStatus();
    }

    public SiswaDTO[] getSiswaDTOList() {
        return getSiswaDTOList();
    }

    public void setStatus(String stringCellValue) {
    }
}