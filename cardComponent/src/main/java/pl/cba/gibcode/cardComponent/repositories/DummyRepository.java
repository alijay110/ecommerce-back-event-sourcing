package pl.cba.gibcode.cardComponent.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.cba.gibcode.cardComponent.entities.DummyEntity;

import java.util.Optional;

@Repository
@Transactional
public interface DummyRepository extends JpaRepository<DummyEntity, Long> {

	Optional<DummyEntity> findOneByAttribute(String attribute);
}
