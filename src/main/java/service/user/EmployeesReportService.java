package service.user;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import model.UserReport;
import repository.report.ReportGenerationRepository;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Stream;

import static database.Constants.Months.NOVEMBER;

public class EmployeesReportService implements ReportGenerationService {

    private final ReportGenerationRepository reportGenerationRepository;

    public EmployeesReportService(ReportGenerationRepository reportGenerationRepository){
        this.reportGenerationRepository = reportGenerationRepository;
    }

    @Override
    public void generateReport() {

        List<UserReport> userReportList = reportGenerationRepository.findAllOrderedByUserIdInAMonth(NOVEMBER);

        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("MonthReport.pdf"));

            document.open();

            PdfPTable table = new PdfPTable(4);
            addTableHeader(table);
            addRows(table, userReportList);
            //addCustomRows(table);

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

    private static void addCustomRows(PdfPTable table)
            throws URISyntaxException, BadElementException, IOException {

        PdfPCell horizontalAlignCell = new PdfPCell(new Phrase("row 2, col 2"));
        horizontalAlignCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(horizontalAlignCell);

        PdfPCell verticalAlignCell = new PdfPCell(new Phrase("row 2, col 3"));
        verticalAlignCell.setVerticalAlignment(Element.ALIGN_BOTTOM);
        table.addCell(verticalAlignCell);
    }
}
