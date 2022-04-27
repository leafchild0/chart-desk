package chart.desk.repositories;

import chart.desk.model.db.TagModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<TagModel, Long> {

    Optional<TagModel> findFirstByName(String name);

}
