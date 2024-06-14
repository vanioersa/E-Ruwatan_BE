package ERuwatan.Tugasbe.dto;

import java.util.List;

public class SiswaDTO {
    private Long id;
    private Long KelasId;
    private String nama_siswa;
    private String nisn;

    public static void saveAll(List<SiswaDTO> datasiswa) {
    }

    public String getTempat() {
        return tempat;
    }

    public void setTempat(String tempat) {
        this.tempat = tempat;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKelasId() {
        return KelasId;
    }

    public void setKelasId(Long kelasId) {
        KelasId = kelasId;
    }

    public String getNama_siswa() {
        return nama_siswa;
    }

    public void setNama_siswa(String nama_siswa) {
        this.nama_siswa = nama_siswa;
    }

    public String getNisn() {
        return nisn;
    }

    public void setNisn(String nisn) {
        this.nisn = nisn;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    private String tempat;
    private String alamat;
}
