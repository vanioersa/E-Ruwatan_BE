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

import javax.persistence.EntityNotFoundException;
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

            float[] columnWidths = {1f, 3f, 2f, 3f, 2f};
            table.setWidths(columnWidths);

            BaseColor darkerLimeColor = new BaseColor(0, 180, 0);

            PdfPCell[] headerCells = new PdfPCell[]{
                    new PdfPCell(new Phrase("No", headerFont)),
                    new PdfPCell(new Phrase("Kelas", headerFont)),
                    new PdfPCell(new Phrase("Tanggal", headerFont)),
                    new PdfPCell(new Phrase("Nama Siswa", headerFont)),
                    new PdfPCell(new Phrase("Status", headerFont))
            };

            for (PdfPCell cell : headerCells) {
                cell.setBackgroundColor(darkerLimeColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(5f);
                cell.setPaddingBottom(5f);
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

                boolean firstEntry = true;
                int siswaStatusCount = piket.getSiswaStatusList().size();

                PdfPCell noCell = new PdfPCell(new Phrase(Integer.toString(no++), regularFont));
                noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                noCell.setRowspan(siswaStatusCount);
                table.addCell(noCell);

                PdfPCell kelasCell = new PdfPCell(new Phrase(kelasNamaKelas, regularFont));
                kelasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                kelasCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                kelasCell.setRowspan(siswaStatusCount);
                table.addCell(kelasCell);

                PdfPCell tanggalCell = new PdfPCell(new Phrase(tanggal, regularFont));
                tanggalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                tanggalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                tanggalCell.setRowspan(siswaStatusCount);
                table.addCell(tanggalCell);

                for (SiswaStatusDTO siswaStatus : piket.getSiswaStatusList()) {
                    PdfPCell siswaCell = new PdfPCell(new Phrase(piketSer.getSiswaById(siswaStatus.getSiswaId()).getNama_siswa(), regularFont));
                    siswaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    siswaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    siswaCell.setPaddingTop(5f);
                    siswaCell.setPaddingBottom(5f);
                    table.addCell(siswaCell);

                    String status = String.join(", ", siswaStatus.getStatusList());
                    Font statusFont = new Font(regularFont.getFamily(), regularFont.getSize(), Font.NORMAL, BaseColor.WHITE);
                    PdfPCell statusCell = new PdfPCell(new Phrase(status, statusFont));
                    statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    statusCell.setPaddingTop(5f);
                    statusCell.setPaddingBottom(5f);

                    switch (status) {
                        case "Masuk":
                            statusCell.setBackgroundColor(new BaseColor(0x00, 0x3C, 0x8C));
                            break;
                        case "Izin":
                            statusCell.setBackgroundColor(new BaseColor(0xF5, 0x7F, 0x17));
                            break;
                        case "Sakit":
                            statusCell.setBackgroundColor(new BaseColor(0x2C, 0x6B, 0x2F));
                            break;
                        case "Alpha":
                            statusCell.setBackgroundColor(new BaseColor(0xC6, 0x28, 0x28));
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
            httpHeaders.setContentDispositionFormData("attachment", "Ekspor-All-piket.pdf");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    public ResponseEntity<byte[]> exportPiketByIdToPdf(Long id) {
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

            float[] columnWidths = {1f, 3f, 2f, 3f, 2f};
            table.setWidths(columnWidths);

            BaseColor darkerLimeColor = new BaseColor(0, 180, 0);

            PdfPCell[] headerCells = new PdfPCell[]{
                    new PdfPCell(new Phrase("No", headerFont)),
                    new PdfPCell(new Phrase("Kelas", headerFont)),
                    new PdfPCell(new Phrase("Tanggal", headerFont)),
                    new PdfPCell(new Phrase("Nama Siswa", headerFont)),
                    new PdfPCell(new Phrase("Status", headerFont))
            };

            for (PdfPCell cell : headerCells) {
                cell.setBackgroundColor(darkerLimeColor);
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell.setPaddingTop(5f);
                cell.setPaddingBottom(5f);
                table.addCell(cell);
            }

            PiketDTO piket = piketSer.getPiketById(id);
            if (piket == null) {
                throw new EntityNotFoundException("Piket dengan ID " + id + " tidak ditemukan");
            }

            KelasDTO kelasDTO = piketSer.getKelasById(piket.getKelasId());
            String kelasNamaKelas = kelasDTO.getKelas() + " - " + kelasDTO.getNama_kelas();
            String tanggal = dateFormat.format(piket.getTanggal());
            int siswaStatusCount = piket.getSiswaStatusList().size();

            PdfPCell noCell = new PdfPCell(new Phrase("1", regularFont));
            noCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            noCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            noCell.setRowspan(siswaStatusCount);
            table.addCell(noCell);

            PdfPCell kelasCell = new PdfPCell(new Phrase(kelasNamaKelas, regularFont));
            kelasCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            kelasCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            kelasCell.setRowspan(siswaStatusCount);
            table.addCell(kelasCell);

            PdfPCell tanggalCell = new PdfPCell(new Phrase(tanggal, regularFont));
            tanggalCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            tanggalCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            tanggalCell.setRowspan(siswaStatusCount);
            table.addCell(tanggalCell);

            for (SiswaStatusDTO siswaStatus : piket.getSiswaStatusList()) {
                PdfPCell siswaCell = new PdfPCell(new Phrase(piketSer.getSiswaById(siswaStatus.getSiswaId()).getNama_siswa(), regularFont));
                siswaCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                siswaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                siswaCell.setPaddingTop(5f);
                siswaCell.setPaddingBottom(5f);
                table.addCell(siswaCell);

                String status = String.join(", ", siswaStatus.getStatusList());
                Font statusFont = new Font(regularFont.getFamily(), regularFont.getSize(), Font.NORMAL, BaseColor.WHITE);
                PdfPCell statusCell = new PdfPCell(new Phrase(status, statusFont));
                statusCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                statusCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                statusCell.setPaddingTop(5f);
                statusCell.setPaddingBottom(5f);

                switch (status) {
                    case "Masuk":
                        statusCell.setBackgroundColor(new BaseColor(0x00, 0x3C, 0x8C));
                        break;
                    case "Izin":
                        statusCell.setBackgroundColor(new BaseColor(0xF5, 0x7F, 0x17));
                        break;
                    case "Sakit":
                        statusCell.setBackgroundColor(new BaseColor(0x2C, 0x6B, 0x2F));
                        break;
                    case "Alpha":
                        statusCell.setBackgroundColor(new BaseColor(0xC6, 0x28, 0x28));
                        break;
                    default:
                        statusCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                        break;
                }

                table.addCell(statusCell);
            }

            document.add(table);
            document.close();

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.parseMediaType("application/pdf"));
            httpHeaders.setContentDispositionFormData("attachment", "Ekspor-Piket-" + id + ".pdf");

            return ResponseEntity.ok()
                    .headers(httpHeaders)
                    .body(outputStream.toByteArray());
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
