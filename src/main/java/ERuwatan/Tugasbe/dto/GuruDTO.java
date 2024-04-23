package ERuwatan.Tugasbe.dto;

public class GuruDTO {
    private Long id;

    private String nama_guru;

    private Long nip;

    private String mapel;

    private String kelasId;

    private String tempat_lahir;

    public GuruDTO() {

    }

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
        return mapel;
    }

    public void setMapel(String mapel) {
        this.mapel = mapel;
    }

    public String getKelasId() {
        return kelasId;
    }

    public void setKelasId(String kelasId) {
        this.kelasId = kelasId;
    }

    public String getTempat_lahir() {
        return tempat_lahir;
    }

    public void setTempat_lahir(String tempat_lahir) {
        this.tempat_lahir = tempat_lahir;
    }

    public GuruDTO(Long id, String nama_guru, Long nip, String mapel, String kelasId, String tempat_lahir) {
        this.id = id;
        this.nama_guru = nama_guru;
        this.nip = nip;
        this.mapel = mapel;
        this.kelasId = kelasId;
        this.tempat_lahir = tempat_lahir;
    }
}
