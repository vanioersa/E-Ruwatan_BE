package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
public class PiketSiswaStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "piket_id")
    private Piket piket;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;

    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Piket getPiket() {
        return piket;
    }

    public void setPiket(Piket piket) {
        this.piket = piket;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
