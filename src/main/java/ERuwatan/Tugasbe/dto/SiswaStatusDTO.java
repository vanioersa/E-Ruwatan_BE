package ERuwatan.Tugasbe.dto;

import java.util.List;

public class SiswaStatusDTO {
    private Long siswaId;
    private List<String> statusList;

    public Long getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(Long siswaId) {
        this.siswaId = siswaId;
    }

    public List<String> getStatusList() {
        return statusList;
    }

    public void setStatusList(List<String> statusList) {
        this.statusList = statusList;
    }
}
