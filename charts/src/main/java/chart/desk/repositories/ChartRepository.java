package chart.desk.repositories;

import chart.desk.model.db.ChartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChartRepository extends JpaRepository<ChartModel, Long> {
    ChartModel findChartModelByNameAndVersionAndUserId(String name, String version, String userId);
}
