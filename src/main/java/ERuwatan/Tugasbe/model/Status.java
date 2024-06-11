package ERuwatan.Tugasbe.model;

import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.Optional;

@Entity
@Table(name = "status")
@Component
public class Status {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;

    private String Status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Siswa getSiswa() {
        return siswa;
    }

    public void setSiswa(Siswa siswa) {
        this.siswa = siswa;
    }

}
