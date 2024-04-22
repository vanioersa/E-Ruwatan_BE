package ERuwatan.Tugasbe.dto;

public class SiswaDTO {
    private Long id;

    private String nama_siswa;

    private Long NISN;

    private String tempat;

    private String kelasId;

    private String alamat;

    public SiswaDTO() {

    }

    public SiswaDTO(Long id, String nama_siswa, Long NISN, String tempat, String kelasId, String alamat) {
        this.id = id;
        this.nama_siswa = nama_siswa;
        this.NISN = NISN;
        this.tempat = tempat;
        this.kelasId = kelasId;
        this.alamat = alamat;
    }

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

    public String getKelasId() {
        return kelasId;
    }

    public void setKelasId(String kelasId) {
        this.kelasId = kelasId;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }
}
