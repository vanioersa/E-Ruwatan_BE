package ERuwatan.Tugasbe.dto;

import java.util.List;

public class PiketDTO {
    private Long kelasId;
    private String tanggal;
    private List<String> status; // Change the type to List<String>
    private List<Long> siswaId;

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

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

    public List<Long> getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(List<Long> siswaId) {
        this.siswaId = siswaId;
    }

    public void setDpiketDTO(PiketDTO dpiketDTO) {
    }

    public void setId(Long id) {
    }

    public void setIdPiket(Long id) {
    }
}