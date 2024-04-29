package ERuwatan.Tugasbe.model;

import javax.persistence.*;
import java.util.Date;

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

    private String masuk;
    private String izin;
    private String sakit;
    private String alpha;

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

    public String getMasuk() {
        return masuk;
    }

    public void setMasuk(String masuk) {
        this.masuk = masuk;
    }

    public String getIzin() {
        return izin;
    }

    public void setIzin(String izin) {
        this.izin = izin;
    }

    public String getSakit() {
        return sakit;
    }

    public void setSakit(String sakit) {
        this.sakit = sakit;
    }

    public String getAlpha() {
        return alpha;
    }

    public void setAlpha(String alpha) {
        this.alpha = alpha;
    }
}