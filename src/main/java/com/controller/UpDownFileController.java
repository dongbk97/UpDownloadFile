package com.controller;

import com.entity.FileData;
import com.entity.Student;
import com.service.FileService;
import com.utils.CreateFile;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;

@RestController
@RequiredArgsConstructor
public class UpDownFileController {

    private final FileService fileService;

//    @GetMapping("/downloadtest")
//    public ResponseEntity<ByteArrayResource> getFile(HttpServletResponse response) throws IOException {
//
//        ByteArrayInputStream workbook = CreateFile.listToExcelFile(
//                Arrays.asList(
//
////                        new Student(14, "Nguyen Van Dong"),
////                        new Student(45, "Nguyen Van Dong"),
////                        new Student(32, "Nguyen Van Dong"),
////                        new Student(56, "Nguyen Van Dong"),
////                        new Student(24, "Nguyen Van Dong"),
////                        new Student(15, "Nguyen Van Dong"),
////                        new Student(62, "Nguyen Van Dong"),
////                        new Student(19, "Nguyen Van Dong"),
////                        new Student(23, "Nguyen Van Dong")
//
//                )
//        );
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=customers.xlsx");
//
//        return ResponseEntity
//                .ok()
//                .headers(headers)
//                .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
//                .body(new ByteArrayResource(workbook.readAllBytes()));
//
//    }


    @PostMapping("/upload")
    public ResponseEntity<?> saveFile(@RequestParam("file") MultipartFile file) throws IOException {

        return ResponseEntity.ok().body(fileService.saveFile(file));
    }
    @GetMapping("/download/{id}")
    public ResponseEntity<?> getFileExcel(@PathVariable("id") Long id) throws IOException {
        FileData fileData=fileService.getFile(id);
        return ResponseEntity.ok()
                 .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileData.getFileName() + "\"")
                .body(fileData.getFileData());
    }
    @GetMapping("/download-pdf/{id}")
    public ResponseEntity<?> getFilePdf(@PathVariable("id") Long id) throws IOException, JRException, ClassNotFoundException {
        Resource fileData=fileService.exportFile(id);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "student" + "\"")
                .body(fileData);
    }
}
