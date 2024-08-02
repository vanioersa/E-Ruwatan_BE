package ERuwatan.Tugasbe.dto;

import java.util.Date;
import java.util.List;

public class PiketDTO {
    private Long id;
    private Long kelasId;
    private Date tanggal;
    private List<SiswaStatusDTO> siswaStatusList;

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

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }

    public List<SiswaStatusDTO> getSiswaStatusList() {
        return siswaStatusList;
    }

    public void setSiswaStatusList(List<SiswaStatusDTO> siswaStatusList) {
        this.siswaStatusList = siswaStatusList;
    }
}
