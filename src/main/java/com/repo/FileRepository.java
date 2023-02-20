package com.repo;

import com.entity.FileData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileData,Long> {
}
