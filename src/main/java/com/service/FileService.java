package com.service;

import com.entity.FileData;
import com.entity.Student;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.repo.FileRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class FileService {


    private final FileRepository fileRepository;


    public FileData saveFile(MultipartFile multipartFile) throws IOException {

        String filename = multipartFile.getOriginalFilename();

        return fileRepository.save(new FileData(filename, multipartFile.getBytes(), LocalDate.now()));

    }

    public FileData getFile(Long id) throws IOException {

        Optional<FileData> optionalFileData = fileRepository.findById(id);

        return optionalFileData.get();


    }

    public Resource exportFile(Long id) throws IOException, ClassNotFoundException, JRException {
        Optional<FileData> optionalFileData = fileRepository.findById(id);
        Resource resource = null;
        Path reportPath = null;
        FileData data = optionalFileData.get();
        List<Student> studentList = convert(data.getFileData());

        InputStream inputStream = FileService.class.getResourceAsStream("/report.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(studentList);
        Map<String, Object> parameter = new HashMap<>();
        parameter.put("createdBy", "Gaurav");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, dataSource);

        JasperExportManager.exportReportToPdfFile(jasperPrint, "student.pdf");
        reportPath = Paths.get("student.pdf");
        resource = new UrlResource(reportPath.toUri());

        return resource;
    }

    private List<Student> convertByteToStudent(byte[] bytes) throws IOException, ClassNotFoundException {


        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);

        ObjectInputStream ois = new ObjectInputStream(bis);

        List<Student> studentList = new ArrayList<>();

        while (bis.available() > 0) {
            Student st = (Student) ois.readObject();
            studentList.add(st);
        }

        ois.close();
        bis.close();
        return studentList;

    }

    private List<Student> convertByteToStudentByJson(byte[] bytes) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Student> personList = objectMapper.readValue(bytes, new TypeReference<List<Student>>(){});

        return personList;
    }




    public List<Student> convert(byte[] bytes) throws IOException {
        List<Student> personList = new ArrayList<>();


        // Đọc file CSV từ các byte
        try (CSVReader csvReader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(bytes)))) {
            String[] headers = csvReader.readNext(); // Lấy các tiêu đề cột

            // Đọc từng dòng trong file CSV và tạo đối tượng Person tương ứng
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                Student person = new Student();
                for (int i = 0; i < headers.length; i++) {
                    switch (headers[i]) {
                        case "id":
                            person.setId(Integer.parseInt(row[i]));
                            break;
                        case "name":
                            person.setName(row[i]);
                            break;
                        case "designation":
                            person.setDesignation(row[i]);
                            break;
                        case "salary":
                            person.setSalary(Double.parseDouble(row[i]));
                            break;
                        default:
                            // Không làm gì cả
                            break;
                    }
                }
                personList.add(person);
            }
        } catch (CsvValidationException e) {
            throw new RuntimeException(e);
        }

        return personList;
    }

}
