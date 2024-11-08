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

class SalesAmountFilterDecorator extends ReportDecorator {
    private double minAmount, maxAmount;

    public SalesAmountFilterDecorator(IReport report, double minAmount, double maxAmount) {
        super(report);
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
    }

    @Override
    public String generate() {
        return report.generate() + " | Отфильтровано по сумме продаж: от " + minAmount + " до " + maxAmount;
    }
}

class UserAttributeFilterDecorator extends ReportDecorator {
    private String attribute;

    public UserAttributeFilterDecorator(IReport report, String attribute) {
        super(report);
        this.attribute = attribute;
    }

    @Override
    public String generate() {
        return report.generate() + " | Отфильтровано по атрибуту пользователя: " + attribute;
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

class ReportBuilder {
    private IReport report;

    public ReportBuilder(IReport report) {
        this.report = report;
    }

    public ReportBuilder withDateFilter(Date startDate, Date endDate) {
        report = new DateFilterDecorator(report, startDate, endDate);
        return this;
    }

    public ReportBuilder withSalesAmountFilter(double minAmount, double maxAmount) {
        report = new SalesAmountFilterDecorator(report, minAmount, maxAmount);
        return this;
    }

    public ReportBuilder withUserAttributeFilter(String attribute) {
        report = new UserAttributeFilterDecorator(report, attribute);
        return this;
    }

    public ReportBuilder withSorting(String criterion) {
        report = new SortingDecorator(report, criterion);
        return this;
    }

    public ReportBuilder withCsvExport() {
        report = new CsvExportDecorator(report);
        return this;
    }

    public ReportBuilder withPdfExport() {
        report = new PdfExportDecorator(report);
        return this;
    }

    public IReport build() {
        return report;
    }
}

public class ReportingSystem {
    public static void main(String[] args) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        IReport salesReport = new SalesReport();
        IReport userReport = new UserReport();

        ReportBuilder builder = new ReportBuilder(salesReport);

        IReport customizedReport = builder
            .withDateFilter(dateFormat.parse("2023-01-01"), dateFormat.parse("2023-12-31"))
            .withSalesAmountFilter(1000, 5000)
            .withSorting("Дата продажи")
            .withCsvExport()
            .build();

        System.out.println(customizedReport.generate());

        builder = new ReportBuilder(userReport);

        IReport userReportWithFilters = builder
            .withUserAttributeFilter("VIP")
            .withSorting("Дата регистрации")
            .withPdfExport()
            .build();

        System.out.println(userReportWithFilters.generate());
    }
}
