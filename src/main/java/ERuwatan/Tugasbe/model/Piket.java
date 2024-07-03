package ERuwatan.Tugasbe.model;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Piket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private Kelas kelas;

    @OneToMany(mappedBy = "piket", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PiketSiswaStatus> siswaStatus;

    @Temporal(TemporalType.DATE)
    private Date tanggal;

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

    public List<PiketSiswaStatus> getSiswaStatus() {
        return siswaStatus;
    }

    public void setSiswaStatus(List<PiketSiswaStatus> siswaStatus) {
        this.siswaStatus = siswaStatus;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
}