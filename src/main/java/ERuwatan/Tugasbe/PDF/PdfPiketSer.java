package ERuwatan.Tugasbe.PDF;

import ERuwatan.Tugasbe.dto.KelasDTO;
import ERuwatan.Tugasbe.dto.PiketDTO;
import ERuwatan.Tugasbe.dto.SiswaStatusDTO;
import ERuwatan.Tugasbe.service.PiketSer;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PdfPiketSer {

    @Autowired
    private PiketSer piketSer;

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    private static final Font boldFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
    private static final Font regularFont = new Font(Font.FontFamily.HELVETICA, 10, Font.NORMAL);
    private static final Font headerFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);
    private static final Font headerFontt = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, BaseColor.WHITE);

    public ResponseEntity<byte[]> exportPiketDataToPdf() {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            Document document = new Document();
            PdfWriter.getInstance(document, outputStream);

            document.open();
            Paragraph title = new Paragraph("DATA PIKETAN", boldFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(Chunk.NEWLINE);

            PdfPTable table = new PdfPTable(5);
            table.setWidthPercentage(100);
            table.setSpacingBefore(10f);
            table.setSpacingAfter(10f);

            PdfPCell[] headerCells = new PdfPCell[]{
                    new PdfPCell(new Phrase("No", headerFontt)),
                    new PdfPCell(new Phrase("Kelas", headerFont)),
                    new PdfPCell(new Phrase("Tanggal", headerFont)),
                    new PdfPCell(new Phrase("Nama Siswa", headerFont)),
                    new PdfPCell(new Phrase("Status", headerFont))
            };

            for (PdfPCell cell : headerCells) {
                cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }

            List<PiketDTO> piketList = piketSer.getAllPiket();
            int no = 1;
            Map<String, Boolean> printedEntries = new HashMap<>();

            for (PiketDTO piket : piketList) {
                KelasDTO kelasDTO = piketSer.getKelasById(piket.getKelasId());
                String kelasNamaKelas = kelasDTO.getKelas() + " - " + kelasDTO.getNama_kelas();
                String tanggal = dateFormat.format(piket.getTanggal());
                String key = piket.getKelasId() + tanggal;

                for (SiswaStatusDTO siswaStatus : piket.getSiswaStatusList()) {
                    if (!printedEntries.containsKey(key)) {
                        PdfPCell noCell = new PdfPCell(new Phrase(Integer.toString(no++), regularFont));
                        noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(noCell);

                        PdfPCell kelasCell = new PdfPCell(new Phrase(kelasNamaKelas, regularFont));
                        kelasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        kelasCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(kelasCell);

                        PdfPCell tanggalCell = new PdfPCell(new Phrase(tanggal, regularFont));
                        tanggalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                        tanggalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                        table.addCell(tanggalCell);

                        printedEntries.put(key, true);
                    } else {
                        table.addCell("");
                        table.addCell("");
                        table.addCell("");
                    }

                    PdfPCell siswaCell = new PdfPCell(new Phrase(piketSer.getSiswaById(siswaStatus.getSiswaId()).getNama_siswa(), regularFont));
                    siswaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    siswaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    table.addCell(siswaCell);

                    String status = String.join(", ", siswaStatus.getStatusList());
                    Font statusFont = new Font(regularFont.getFamily(), regularFont.getSize(), Font.NORMAL, BaseColor.WHITE);
                    PdfPCell statusCell = new PdfPCell(new Phrase(status, statusFont));
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

                    switch (status) {
                        case "Masuk":
                            statusCell.setBackgroundColor(BaseColor.BLUE);
                            break;
                        case "Izin":
                            statusCell.setBackgroundColor(BaseColor.YELLOW);
                            break;
                        case "Sakit":
                            statusCell.setBackgroundColor(BaseColor.GREEN);
                            break;
                        case "Alpha":
                            statusCell.setBackgroundColor(BaseColor.RED);
                            break;
                        default:
                            statusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                            break;
                    }

                    table.addCell(statusCell);
                }
            }

            document.add(table);
            document.close();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            httpHeaders.setContentDispositionFormData("attachment", "piket_data.pdf");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
