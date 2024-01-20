package com.hust.interviewmanagement.service.impl;

import com.google.gson.*;
import com.hust.interviewmanagement.service.FileService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

@Service
public class FileServiceImpl<T> implements FileService<T> {
    private static final String SOURCE_TARGET = "src/main/resources/static/files/";

    @Override
    public List<T> importExcel(MultipartFile file, Class<T> clazz) {
        List<String> key = new ArrayList<>();
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        List<T> t = new ArrayList<>();
        JSONObject jsonObject = new JSONObject();
        try (InputStream inputStream = file.getInputStream()) {
            String fileName = Objects.requireNonNull(file.getOriginalFilename());
            Workbook workbook = getWorkbook(inputStream, fileName);
            Sheet sheet = workbook.getSheetAt(0);
            boolean isCheck = true;
            for (Row row : sheet) {
                int rowIndex = 0;
                for (Cell cell : row) {
                    if (isCheck) {
                        key.add(String.valueOf(cell));
                    } else {
                        jsonObject.put(key.get(rowIndex), cell);
                        rowIndex++;
                    }
                }
                if (!isCheck) {
                    t.add(gson.fromJson(String.valueOf(jsonObject), clazz));
                }
                isCheck = false;
            }
        } catch (IOException e) {
            throw new IllegalArgumentException("Don't not import");
        }
        return t;
    }

    @Override
    public void export(HttpServletResponse resp, List<T> t) {
        setupExport(resp, t.get(0));
        try (Workbook workbook = new XSSFWorkbook();
             ServletOutputStream output = resp.getOutputStream()) {
            Sheet sheet = workbook.createSheet(t.get(0).getClass().getSimpleName());
            writeHeader(t, workbook, sheet);
            writeData(t, workbook, sheet);
            workbook.write(output);
        } catch (IOException e) {
            throw new IllegalArgumentException("Don't export");
        }
    }

    @Override
    public void saveFile(MultipartFile file, Long id) throws IOException {
        final String uploadDir = SOURCE_TARGET + id;
        final String fileName = Objects.requireNonNull(file.getOriginalFilename());
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            Files.createDirectory(path);
        }
        try (InputStream inputStream = file.getInputStream()) {
            Path pathFile = path.resolve(fileName);
            Files.copy(inputStream, pathFile, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Don't save.");
        }
    }

    @Override
    public byte[] downloadFile(Long id, String fileName) throws IOException {
        final String src = SOURCE_TARGET + id + "/" + fileName;
        try (InputStream inputStream = new FileInputStream(src)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            throw new IOException("Don't found file");
        }
    }

    private void setupExport(HttpServletResponse resp, T t) {
        resp.setContentType("application/octet-stream");
        final String keyHeader = "Content-Disposition";
        Date date = new Date();
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        final String dateFormat = simpleDateFormat.format(date);
        final String fileName = dateFormat + "_" + t.getClass().getSimpleName() + ".xlsx";
        final String valueHeader = "attachment; filename=" + fileName;
        resp.setHeader(keyHeader, valueHeader);
    }

    private void writeHeader(List<T> t, Workbook workbook, Sheet sheet) {
        Row row = sheet.createRow(0);
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        String a = gson.toJson(t.get(0));
        JsonObject json = (JsonObject) JsonParser.parseString(a);
        Set<String> keys = json.keySet();
        int i = 0;
        for (String key : keys) {
            Cell cell = row.createCell(i);
            cell.setCellValue(key);
            cell.setCellStyle(styleHeader(workbook));
            i++;
        }
    }

    private CellStyle styleHeader(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = (XSSFFont) workbook.createFont();
        font.setFontHeight(16);
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private void writeData(List<T> t, Workbook workbook, Sheet sheet) {
        int rowNumber = 1;
        Gson gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
                .create();
        for (T e : t) {
            Row row = sheet.createRow(rowNumber);
            String json = gson.toJson(e);
            JsonObject jsonObject = (JsonObject) JsonParser.parseString(json);
            int cellNumber = 0;
            for (Entry<String, JsonElement> jsonElement : jsonObject.entrySet()) {
                Cell cell = row.createCell(cellNumber);
                cell.setCellValue(jsonElement.getValue().getAsString());
                sheet.autoSizeColumn(cellNumber);
                cell.setCellStyle(styleData(workbook));
                cellNumber++;
            }
            rowNumber++;
        }
    }

    private CellStyle styleData(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        return cellStyle;
    }


    private Workbook getWorkbook(InputStream inputStream, String fileName) throws IOException {
        if (fileName.endsWith(".xlsx")) {
            return new XSSFWorkbook(inputStream);
        } else if (fileName.endsWith(".xls")) {
            return new HSSFWorkbook(inputStream);
        } else {
            throw new IllegalArgumentException("The specified file is not Excel file");
        }
    }

    private static class LocalDateTypeAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

        @Override
        public LocalDate deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            return LocalDate.parse(jsonElement.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE);
        }

        @Override
        public JsonElement serialize(LocalDate localDate, Type type, JsonSerializationContext jsonSerializationContext) {
            return new JsonPrimitive(
                    localDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            );
        }
    }
}
