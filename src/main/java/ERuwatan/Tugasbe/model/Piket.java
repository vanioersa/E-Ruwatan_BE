package ERuwatan.Tugasbe.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Piket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    @ManyToMany
    @JoinTable(
            name = "piket_siswa",
            joinColumns = @JoinColumn(name = "piket_id"),
            inverseJoinColumns = @JoinColumn(name = "siswa_id")
    )
    private List<Siswa> siswa;

    @ElementCollection
    @CollectionTable(name = "piket_status", joinColumns = @JoinColumn(name = "piket_id"))
    @Column(name = "status")
    private List<String> status;

    private String tanggal;

    // Getter dan Setter
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

    public List<Siswa> getSiswa() {
        return siswa;
    }

    public void setSiswa(List<Siswa> siswa) {
        this.siswa = siswa;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
