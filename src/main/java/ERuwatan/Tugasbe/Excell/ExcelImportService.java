package ERuwatan.Tugasbe.Excell;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

public class ExcelImportService {
    public void importDataFromExcel(MultipartFile file) throws IOException {
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0); // Assume the data is in the first sheet

            Iterator<Row> iterator = sheet.iterator();
            while (iterator.hasNext()) {
                Row currentRow = iterator.next();

                // Skip header row
                if (currentRow.getRowNum() == 0) {
                    continue;
                }

                // Process each cell in the row
                Iterator<Cell> cellIterator = currentRow.iterator();
                while (cellIterator.hasNext()) {
                    Cell currentCell = cellIterator.next();
                    // Process cell data here
                    System.out.print(currentCell.toString() + "\t");
                }
                System.out.println(); // Move to the next row
            }
            workbook.close();
        } catch (IOException e) {
            throw new IOException("Failed to read Excel file: " + e.getMessage());
        }
    }
}
