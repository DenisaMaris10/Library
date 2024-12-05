package service.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.UserReport;
import repository.report.ReportGenerationRepository;

import java.io.FileOutputStream;
import java.util.List;
import java.util.stream.Stream;

public class EmployeesAllSalesReportService implements ReportGenerationService {

    private final ReportGenerationRepository reportGenerationRepository;

    public EmployeesAllSalesReportService(ReportGenerationRepository reportGenerationRepository){
        this.reportGenerationRepository = reportGenerationRepository;
    }

    @Override
    public void generateReport(String month) {

        List<UserReport> userReportList = reportGenerationRepository.findAllOrderedByUserIdInAMonth(month);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("MonthReport.pdf"));

            document.open();

            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            addRows(table, userReportList);

            document.add(table);
            document.close();
        }catch (Exception e){
            System.out.println("Report generation error!");
            e.printStackTrace();
        }
    }

    private static void addTableHeader(PdfPTable table) {
        Stream.of("UserId", "Username", "Total Price", "Timestamp")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(2);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }

    private static void addRows(PdfPTable table, List<UserReport> userReportList) {
        userReportList
                .forEach(userReport->addRow(table, userReport));

    }

    private static void addRow(PdfPTable table, UserReport userReport){
        PdfPCell userIdCell = new PdfPCell(new Phrase(userReport.getUserId().toString()));
        userIdCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(userIdCell);

        PdfPCell usernameCell = new PdfPCell(new Phrase(userReport.getUsername()));
        usernameCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(usernameCell);

        PdfPCell totalPriceCell = new PdfPCell(new Phrase(userReport.getTotalPrice().toString()));
        totalPriceCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(totalPriceCell);

        PdfPCell timestampCell = new PdfPCell(new Phrase(userReport.getTimestamp().toString()));
        timestampCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(timestampCell);
    }

}
