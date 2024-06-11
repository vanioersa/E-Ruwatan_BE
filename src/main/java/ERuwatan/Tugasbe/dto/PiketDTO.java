package ERuwatan.Tugasbe.dto;

import java.util.List;

public class PiketDTO {
    private Long id;
    private Long kelasId;
    private String tanggal;

    private StatusDTO status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getKelasId() {
        return kelasId;
    }

    public void setKelasId(Long kelasId) {
        this.kelasId = kelasId;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }

    public void setIdPiket(Long id) {
    }

    public void setSiswaId(List<Long> siswaIdList) {
    }
}