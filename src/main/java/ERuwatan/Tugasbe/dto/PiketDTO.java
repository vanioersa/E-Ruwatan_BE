package ERuwatan.Tugasbe.dto;

import java.util.List;

public class PiketDTO {
    private Long id;
    private Long kelasId;

    private Long siswaId;
    private String tanggal;
    private List<String> status; // Change from String to List<String>

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

    public List<String> getStatus() { // Getter returns List<String>
        return status;
    }

    public void setStatus(List<String> status) { // Setter accepts List<String>
        this.status = status;
    }

    public void setIdPiket(Long id) {
    }

    public Long getSiswaId() {
        return siswaId;
    }


    public void setSiswaId(List<Long> siswaIdList) {
    }
}
