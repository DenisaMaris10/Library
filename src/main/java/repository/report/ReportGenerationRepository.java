package repository.report;

import model.UserReport;

import java.util.List;

public interface ReportGenerationRepository {
    List<UserReport> findAllOrderedByUserIdInAMonth(String month);
}
