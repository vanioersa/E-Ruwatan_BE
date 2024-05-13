package ERuwatan.Tugasbe.dto;

public class PenilaianDTO {
    private Long id;
    private Long siswaId;
    private Long kelasId;
    private String nilai;
    private String deskripsi;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(Long siswaId) {
        this.siswaId = siswaId;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }

    public String getNilai() {
        return nilai;
    }

    public void setNilai(String nilai) {
        this.nilai = nilai;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
