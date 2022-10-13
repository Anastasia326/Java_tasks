import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

public class DrawTables<T extends Format> {
    public XSSFWorkbook book;
    public XSSFCellStyle headCell;
    public XSSFCellStyle cell;

    public void work(Pair<ArrayList<String>,ArrayList<T>> data, String nameFile, int size) throws Exception{
        //создаем файл вывода
        File new_file = new File(nameFile);
        FileOutputStream fileOutputStream = new FileOutputStream(new_file);
        //создаем объект
        book = new XSSFWorkbook();
        //создаем лист
        XSSFSheet sheet = book.createSheet("Output");
        sheet.createFreezePane(0, 1);
        //для закрашивания и создания ячеек нужного вида
        cell = book.createCellStyle();
        Font font = book.createFont();
        font.setFontHeightInPoints((short) 10);
        cell.setFont(font);

        headCell = book.createCellStyle();
        //заголовок со стилем отличным от обычных
        headCell.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headCell.setAlignment(HorizontalAlignment.CENTER);
        font = book.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 10);
        headCell.setFont(font);

        //создаем заголовок таблицы
        XSSFRow row = sheet.createRow(0);
        int columns = data.getFirst().size();
        for(int i = 0; i < columns; ++i) {
            Cell cell = row.createCell(i);
            cell.setCellValue(data.getFirst().get(i));
            cell.setCellStyle(headCell);
        }
        //создаем обычные ячейки
        for(int i = 1; i < size + 1; ++i) {
            XSSFRow newRow = sheet.createRow(i);
            for(int j = 0; j < columns; ++j) {
                Cell cellNow = newRow.createCell(j);
                cellNow.setCellValue(data.getSecond().get(i - 1).getData().get(j).toString());
                cellNow.setCellStyle(cell);
            }
        }
        //подгоняем размер
        for (int i = 0; i < columns; ++i) {
            sheet.autoSizeColumn(i);
        }
        book.write(fileOutputStream);
        fileOutputStream.close();
    }
}
