package ERuwatan.Tugasbe.dto;

import java.util.List;

public class PiketDTO {
    private Long id;
    private Long kelasId;
    private List<SiswaStatusDTO> siswaStatusList;
    private String tanggal;

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

    public List<SiswaStatusDTO> getSiswaStatusList() {
        return siswaStatusList;
    }

    public void setSiswaStatusList(List<SiswaStatusDTO> siswaStatusList) {
        this.siswaStatusList = siswaStatusList;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }
}
