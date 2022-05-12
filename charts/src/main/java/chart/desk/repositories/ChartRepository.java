package chart.desk.repositories;

import chart.desk.model.db.ChartModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChartRepository extends JpaRepository<ChartModel, Long> {
    Optional<ChartModel> findChartModelByUserNameAndNameAndVersion(String userName, String name, String version);

    Optional<ChartModel> findChartModelByUserNameAndId(String userName, Long id);

    List<ChartModel> findAllByUserName(String userName);

    List<ChartModel> findAllByUserNameAndName(String userName, String name);
}
