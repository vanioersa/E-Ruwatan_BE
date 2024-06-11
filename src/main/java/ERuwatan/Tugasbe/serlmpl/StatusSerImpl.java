package ERuwatan.Tugasbe.serlmpl;

import ERuwatan.Tugasbe.model.Status;
import ERuwatan.Tugasbe.repository.StatusRepo;
import ERuwatan.Tugasbe.service.StatusSer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StatusSerImpl implements StatusSer {

    @Autowired
    private StatusRepo statusRepo;

    @Override
    public Status createStatus(Status status) {
        return statusRepo.save(status);
    }

    @Override
    public Optional<Status> getStatusById(Long id) {
        return statusRepo.findById(id);
    }

    @Override
    public List<Status> getAllStatus() {
        return statusRepo.findAll();
    }

    @Override
    public Status updateStatus(Long id, Status updatedStatus) {
        Optional<Status> optionalStatus = statusRepo.findById(id);
        if (optionalStatus.isPresent()) {
            Status status = optionalStatus.get();
            status.setSiswa(updatedStatus.getSiswa());
            status.setId(updatedStatus.getId());
            return statusRepo.save(status);
        }
        return null;
    }

    @Override
    public void deleteStatus(Long id) {
        statusRepo.deleteById(id);
    }
}