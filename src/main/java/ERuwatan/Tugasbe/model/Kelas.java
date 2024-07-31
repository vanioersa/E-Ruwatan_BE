package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
@Table(name = "Kelas")
public class Kelas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String kelas;
    private String nama_kelas;

    public Kelas() {

    }

    public Kelas(String kelas, String namaKelas) {
        this.kelas = kelas;
        this.nama_kelas = namaKelas;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKelas() {
        return kelas;
    }

    public void setKelas(String kelas) {
        this.kelas = kelas;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }

    @Override
    public String toString() {
        return "Kelas{" +
                "id=" + id +
                ", kelas='" + kelas + '\'' +
                ", nama_kelas='" + nama_kelas + '\'' +
                '}';
    }
}
