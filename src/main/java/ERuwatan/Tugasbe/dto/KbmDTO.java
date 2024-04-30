package ERuwatan.Tugasbe.dto;

public class KbmDTO {
    private Long id;
    private Long namaId;
    private Long kelasId;
    private String jam_masuk;
    private String jam_pulang;
    private String materi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNamaId() {
        return namaId;
    }

    public void setNamaId(Long namaId) {
        this.namaId = namaId;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
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
