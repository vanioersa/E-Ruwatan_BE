package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
@Table(name = "Kelas")
public class KelasModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }

    public Long getKelas() {
        return kelas;
    }

    public void setKelas(Long kelas) {
        this.kelas = kelas;
    }

    @Column
    private String nama_kelas;

    private Long kelas;

}
