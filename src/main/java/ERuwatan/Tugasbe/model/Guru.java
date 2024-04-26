package ERuwatan.Tugasbe.model;

import javax.persistence.*;

@Entity
@Table(name = "Guru")
public class Guru {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String nama_guru;

    @Column
    private Long nip;

    @Column
    private String Mapel;

    @Column
    private String tempat_lahir;

    @ManyToOne
    @JoinColumn(name = "kelas_id")
    private Kelas kelasModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_guru() {
        return nama_guru;
    }

    public void setNama_guru(String nama_guru) {
        this.nama_guru = nama_guru;
    }

    public Long getNip() {
        return nip;
    }

    public void setNip(Long nip) {
        this.nip = nip;
    }

    public String getMapel() {
        return Mapel;
    }

    public void setMapel(String mapel) {
        Mapel = mapel;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public Kelas getKelasModel() {
        return kelasModel;
    }

    public void setKelasModel(Kelas kelasModel) {
        this.kelasModel = kelasModel;
    }
}
