package chart.desk.repositories;

import chart.desk.model.db.SourceModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SourceRepository extends JpaRepository<SourceModel, Long> {

    Optional<SourceModel> findFirstByUrl(String url);

}
