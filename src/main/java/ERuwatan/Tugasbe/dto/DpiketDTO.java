package ERuwatan.Tugasbe.dto;

import java.util.List;

public class DpiketDTO {

    private List<Long> siswaId;
    private List<String> status;

    public List<Long> getSiswaId() {
        return siswaId;
    }

    public void setSiswaId(List<Long> siswaId) {
        this.siswaId = siswaId;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}
