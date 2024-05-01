package ERuwatan.Tugasbe.dto;

import java.util.Date;

public class PiketDTO {
    private Long id;
    private Long kelasId;
    private Long siswaId;
    private String tanggal;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }

    public Long getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(Long siswaId) {
        this.siswaId = siswaId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}