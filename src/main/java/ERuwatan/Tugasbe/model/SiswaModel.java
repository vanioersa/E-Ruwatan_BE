package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
@Table(name = "Siswa")
public class SiswaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nama_siswa;

    @Column
    private Long NISN;

    @Column
    private String tempat;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private KelasModel kelasModel;

    @Column
    private String alamat;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_siswa() {
        return nama_siswa;
    }

    public void setNama_siswa(String nama_siswa) {
        this.nama_siswa = nama_siswa;
    }

    public Long getNISN() {
        return NISN;
    }

    public void setNISN(Long NISN) {
        this.NISN = NISN;
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public KelasModel getKelasModel() {
        return kelasModel;
    }

    public void setKelasModel(KelasModel kelasModel) {
        this.kelasModel = kelasModel;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
