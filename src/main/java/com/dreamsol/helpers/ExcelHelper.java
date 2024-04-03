package com.dreamsol.helpers;

import com.dreamsol.dto.DocumentSingleDataResponseDto;
import com.dreamsol.entities.Department;
import com.dreamsol.entities.Document;
import com.dreamsol.entities.User;
import com.dreamsol.entities.UserType;
import com.dreamsol.response.ApiResponse;
import com.dreamsol.response.ExcelUploadResponse;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class ExcelHelper<T>
{
    public static boolean checkExcelFormat(MultipartFile file)
    {
        String contentType = file.getContentType();
        return contentType == null || !contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
    }
    public static <T> ByteArrayInputStream convertListToExcel(Class<T> entityName,List<T> list, Map<String,String> headersMap, String sheetName)
    {
        try{
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet(sheetName);

            // Create a CellStyle
            CellStyle style = workbook.createCellStyle();
            style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style.setAlignment(HorizontalAlignment.CENTER);
            style.setVerticalAlignment(VerticalAlignment.CENTER);

            // Create a Font
            Font font = workbook.createFont();
            font.setFontName("Arial");
            font.setFontHeightInPoints((short) 10);
            font.setColor(IndexedColors.BLACK.getIndex());
            font.setBold(true);
            style.setFont(font);

            Map<Integer,String> cellHeaderMappings = new HashMap<>();
            Set<String> headersNameSet = headersMap.keySet();
            int cellIndex = 0;
            Row headerRow = sheet.createRow(0);
            for(String headerName : headersNameSet)
            {
                Cell cell = headerRow.createCell(cellIndex);
                cell.setCellValue(headerName);
                cell.setCellStyle(style);
                cellHeaderMappings.put(cellIndex++,headerName);
            }

            int rowIndex = 1;

            for(T t : list)
            {
                Row row = sheet.createRow(rowIndex++);
                for(Integer index : cellHeaderMappings.keySet())
                {
                    Cell cell = row.createCell(index);
                    Field field = entityName.getDeclaredField(headersMap.get(cellHeaderMappings.get(index)));
                    field.setAccessible(true);
                    String fieldType = field.getType().getSimpleName();
                    if(fieldType.equalsIgnoreCase("string"))
                        cell.setCellValue((String) field.get(t));
                    else if(fieldType.equalsIgnoreCase("long"))
                        cell.setCellValue((long) field.get(t));
                    else if(fieldType.equalsIgnoreCase("boolean"))
                        cell.setCellValue((boolean) field.get(t));
                }
            }
            workbook.write(byteArrayOutputStream);
            return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        }catch(Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    public static Resource getExcelSample(Map<String,String> headers,Map<String,String> type,Map<String,String> examples, String sheetName)
    {
        try
        {
            Workbook workbook = new XSSFWorkbook();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            Sheet sheet = workbook.createSheet(sheetName);
            Map<Integer,String> columnHeaderMappings = new HashMap<>();
            int cellIndex = 0;
            Set<String> headerKeySet = headers.keySet();
            for (String headerKey : headerKeySet)
            {
                columnHeaderMappings.put(cellIndex++,headerKey);
            }
            CellStyle style1 = workbook.createCellStyle();
            style1.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
            style1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style1.setAlignment(HorizontalAlignment.CENTER);

            Font font1 = workbook.createFont();
            font1.setFontName("arial");
            font1.setItalic(true);
            font1.setBold(true);
            font1.setColor(IndexedColors.BLACK.getIndex());
            style1.setFont(font1);

            int maxLength = headers.size();
            Row row1 = sheet.createRow(0);
            for(int index = 0 ; index<maxLength ; index++)
            {
                Cell cell = row1.createCell(index);
                cell.setCellValue(type.get(columnHeaderMappings.get(index)));
                cell.setCellStyle(style1);
            }

            // Create a CellStyle
            CellStyle style2 = workbook.createCellStyle();
            style2.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            style2.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            style2.setAlignment(HorizontalAlignment.CENTER);
            style2.setVerticalAlignment(VerticalAlignment.CENTER);

            // Create a Font
            Font font2 = workbook.createFont();
            font2.setFontName("Arial");
            font2.setFontHeightInPoints((short) 10);
            font2.setColor(IndexedColors.BLACK.getIndex());
            font2.setBold(true);
            style2.setFont(font2);

            Row row2 = sheet.createRow(1);
            for(int index = 0 ; index<maxLength ; index++)
            {
                Cell cell = row2.createCell(index);
                cell.setCellValue(columnHeaderMappings.get(index));
                cell.setCellStyle(style2);
            }

            CellStyle style3 = workbook.createCellStyle();
            Font font3 = workbook.createFont();
            font3.setColor(IndexedColors.GREY_50_PERCENT.getIndex());
            font3.setItalic(true);
            style3.setFont(font3);
            Row row3 = sheet.createRow(2);
            for(int index = 0 ; index<headers.size() ; index++)
            {
                Cell cell = row3.createCell(index);
                cell.setCellValue(examples.get(columnHeaderMappings.get(index)));
                cell.setCellStyle(style3);
            }

            workbook.write(byteArrayOutputStream);
            workbook.close();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            return new InputStreamResource(byteArrayInputStream);
        }catch(IOException e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    public <T> List<T> convertExcelToList(MultipartFile excelFile,Map<String,String> headersMap, Class<T> entityName)
    {
        List<T> entityList = new ArrayList<>();
        try
        {
            InputStream inputStream = excelFile.getInputStream();
            XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
            XSSFSheet sheet = workbook.getSheetAt(0);
            Map<Integer,String> cellHeaderMappings = new HashMap<>();
            int colIndex=0;
            for(Cell cell : sheet.getRow(1))
            {
                cellHeaderMappings.put(colIndex++,cell.getStringCellValue());
            }

            for(Row row : sheet)
            {
                if(row.getRowNum()<=2)
                    continue;
                T entity = entityName.getDeclaredConstructor().newInstance();
                Set<Integer> cellHeaderKeySet = cellHeaderMappings.keySet();
                for(Integer cellIndex : cellHeaderKeySet)
                {
                    Cell cell = row.getCell(cellIndex);
                    if (cell == null) {
                        continue;
                    }
                    String fieldName = headersMap.get(cellHeaderMappings.get(cellIndex));
                    Field field = entityName.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    String fieldTypeName = field.getType().getSimpleName();
                    switch(fieldTypeName.toLowerCase())
                    {
                        case "string":
                            if(cell.getCellType() == CellType.STRING)
                            {
                                field.set(entity,cell.getStringCellValue());
                                break;
                            }
                            field.set(entity,"invalid");
                            break;
                        case "long":
                            if(cell.getCellType() == CellType.NUMERIC)
                            {
                                long longCellValue = (long) cell.getNumericCellValue();
                                field.set(entity,longCellValue);
                                break;
                            }
                            field.set(entity,0L);
                            break;
                        case "boolean":
                            if(cell.getCellType() == CellType.BOOLEAN)
                            {
                                boolean booleanCellValue = cell.getBooleanCellValue();
                                field.set(entity,booleanCellValue);
                                break;
                            }
                            field.set(entity,false);
                            break;
                        default:
                            field.set(entity,null);
                            break;
                    }
                }
                entityList.add(entity);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
        return entityList;
    }
    public static Resource getAutoGeneratedExcelFile()
    {
        Map<String,String> headersMap = ExcelHeadersInfo.getDepartmentHeadersMap();
        String sheetName = "autogenerated_department_data";
        List<Department> departmentList = new ArrayList<>();
        for(int i=0;i<100;i++)
        {
            String randomID = UUID.randomUUID().toString();
            String departmentName = randomID.substring(randomID.lastIndexOf('-')+1);
            String departmentCode = randomID.substring(0,7);
            Department department = new Department();
            department.setDepartmentName(departmentName);
            department.setDepartmentCode(departmentCode);
            department.setStatus(true);
            departmentList.add(department);
        }
        ByteArrayInputStream byteArrayInputStream = ExcelHelper.convertListToExcel(Department.class,departmentList,headersMap,sheetName);
        return new InputStreamResource(byteArrayInputStream);
    }
}
