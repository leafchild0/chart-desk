package chart.desk.repositories;

import chart.desk.model.db.ChartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartRepository extends JpaRepository<ChartModel, Long>, RevisionRepository<ChartModel, Long, Long> {
    Optional<ChartModel> findChartModelByNameAndUserId(String name, String userId);

    List<ChartModel> findAllByUserId(String userId);
}
