package com.example.jasper.util;

import java.io.File;
import java.io.FileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class FileUtils {

  public File getFile(String filePath) throws FileNotFoundException {
    return ResourceUtils.getFile(filePath);
  }

}
