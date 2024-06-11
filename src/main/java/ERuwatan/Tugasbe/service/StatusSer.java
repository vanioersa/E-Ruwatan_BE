package ERuwatan.Tugasbe.service;

import ERuwatan.Tugasbe.model.Status;

import java.util.List;
import java.util.Optional;

public interface StatusSer {
    Status createStatus(Status status);
    Optional<Status> getStatusById(Long id);
    List<Status> getAllStatus();
    Status updateStatus(Long id, Status updatedStatus);
    void deleteStatus(Long id);
}