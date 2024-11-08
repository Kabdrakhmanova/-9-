import java.util.*;
import java.text.SimpleDateFormat;

interface IReport {
    String generate();
}

class SalesReport implements IReport {
    public String generate() {
        return "Данные отчета по продажам";
    }
}

class UserReport implements IReport {
    public String generate() {
        return "Данные отчета по пользователям";
    }
}

abstract class ReportDecorator implements IReport {
    protected IReport report;

    public ReportDecorator(IReport report) {
        this.report = report;
    }

    public String generate() {
        return report.generate();
    }
}

class DateFilterDecorator extends ReportDecorator {
    private Date startDate, endDate;

    public DateFilterDecorator(IReport report, Date startDate, Date endDate) {
        super(report);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    @Override
    public String generate() {
        return report.generate() + " | Отфильтровано по датам: " + startDate + " - " + endDate;
    }
}

class SortingDecorator extends ReportDecorator {
    private String criterion;

    public SortingDecorator(IReport report, String criterion) {
        super(report);
        this.criterion = criterion;
    }

    @Override
    public String generate() {
        return report.generate() + " | Отсортировано по: " + criterion;
    }
}

class CsvExportDecorator extends ReportDecorator {
    public CsvExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return report.generate() + " | Экспортировано в CSV";
    }
}

class PdfExportDecorator extends ReportDecorator {
    public PdfExportDecorator(IReport report) {
        super(report);
    }

    @Override
    public String generate() {
        return report.generate() + " | Экспортировано в PDF";
    }
}

public class ReportingSystem {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        IReport salesReport = new SalesReport();
        IReport userReport = new UserReport();
        
        IReport filteredSalesReport = new DateFilterDecorator(salesReport, dateFormat.parse("2023-01-01"), dateFormat.parse("2023-12-31"));
        IReport sortedUserReport = new SortingDecorator(userReport, "Дата регистрации");
        IReport csvExportedReport = new CsvExportDecorator(filteredSalesReport);
        IReport pdfExportedReport = new PdfExportDecorator(sortedUserReport);
        
        System.out.println(csvExportedReport.generate());
        System.out.println(pdfExportedReport.generate());
    }
}
