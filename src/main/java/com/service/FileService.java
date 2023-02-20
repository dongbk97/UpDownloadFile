package com.service;

import com.entity.FileData;
import com.repo.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {


    private final FileRepository fileRepository;


    public FileData saveFile(MultipartFile multipartFile) throws IOException {

        String filename = multipartFile.getOriginalFilename();

        return fileRepository.save(new FileData(filename, multipartFile.getBytes(), LocalDate.now()));

    }

    public FileData getFile(Long id) throws IOException {

        Optional<FileData> optionalFileData= fileRepository.findById(id);

        return optionalFileData.get();


    }


}
