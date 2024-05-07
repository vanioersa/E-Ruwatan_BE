package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
public class Kbm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public UserModel getNama() {
        return nama;
    }

    public void setNama(UserModel nama) {
        this.nama = nama;
    }

    @ManyToOne
    @JoinColumn(name = "guru_id", nullable = false)
    private UserModel nama;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    private String jam_masuk;
    private String jam_pulang;
    private String materi;

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

    public String getJam_masuk() {
        return jam_masuk;
    }

    public void setJam_masuk(String jam_masuk) {
        this.jam_masuk = jam_masuk;
    }

    public String getJam_pulang() {
        return jam_pulang;
    }

    public void setJam_pulang(String jam_pulang) {
        this.jam_pulang = jam_pulang;
    }

    public String getMateri() {
        return materi;
    }

    public void setMateri(String materi) {
        this.materi = materi;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    private String keterangan;
}
