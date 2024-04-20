package ERuwatan.Tugasbe.dto;

public class kelasDTO {
    private Long id;

private String nama_kelas;

    public Long getId() {
        return id;
    }

    public kelasDTO() {

    }

    public kelasDTO(Long id, String nama_kelas, Long kelas) {
        this.id = id;
        this.nama_kelas = nama_kelas;
        this.kelas = kelas;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNama_kelas() {
        return nama_kelas;
    }

    public void setNama_kelas(String nama_kelas) {
        this.nama_kelas = nama_kelas;
    }

    public Long getKelas() {
        return kelas;
    }

    public void setKelas(Long kelas) {
        this.kelas = kelas;
    }

    private Long kelas;
}
