package kz.stud.demo.repository;

import kz.stud.demo.model.ERole;
import kz.stud.demo.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends MongoRepository<Role, Long> {
    Optional<Role> findByName(ERole name);
}
