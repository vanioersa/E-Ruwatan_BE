package ERuwatan.Tugasbe.dto;

import java.util.List;

public class PiketDTO {
    private Long id;
    private Long kelasId;
    private String tanggal;
    private List<DpiketDTO> dpiketDTOList;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<DpiketDTO> getDpiketDTOList() {
        return dpiketDTOList;
    }

    public void setDpiketDTOList(List<DpiketDTO> dpiketDTOList) {
        this.dpiketDTOList = dpiketDTOList;
    }

    public void setSiswaId(Long aLong) {
    }
}