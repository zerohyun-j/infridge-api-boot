package com.inthefridges.api.service;

import com.inthefridges.api.entity.InFridgeFile;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultFileService implements FileService{

    private final FileRepository repository;
    @Value("${file.dir}")
    private String FILE_DIR;
    private final HttpServletRequest request;

    @Override
    public Long create(MultipartFile requestFile) {

        if(requestFile.isEmpty())
            throw new ServiceException(ExceptionCode.MISSING_DATA);

        // resources/dev/2023/09와 같은 path 반환
        String dateFolderPath = createOrGetFolderPathByDate();
        StringBuilder saveDirectoryPath = new StringBuilder(getRootDirectory())
                                    .append(dateFolderPath);

        // 클라이언트가 업로드한 파일 이름 추출
        String originalFileName = requestFile.getOriginalFilename();

        // 확장자 추출
        String fileExtension = originalFileName.substring(originalFileName.lastIndexOf("."));

        // DB 저장
        InFridgeFile newFile = InFridgeFile.builder()
                .path(dateFolderPath)
                .originName(originalFileName)
                .build();
        repository.save(newFile);

        String newFileId = String.valueOf(newFile.getId());
        try {
            String fullPath = saveDirectoryPath.append(newFileId).append(fileExtension).toString();
            File destination = new File(fullPath);
            requestFile.transferTo(destination);
        } catch (IOException e) {
            throw new ServiceException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }

        return newFile.getId();
    }

    /**
     * 년도, 월별로 폴더 생성 TODO: 서비스 환경에서는 루트 디렉터리 사용하지 않도록 리팩토링 하기.
     * */
    private String createOrGetFolderPathByDate() {

        /** 현재 년월을 문자열로 포맷팅,
         *  File.separator : 현재 실행되는 운영체제에 따른 기본 디렉터리 구분 문자(separator)를 반환
         * */
        String formattedDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
        String systemFolderPath = formattedDate.replace("/", File.separator);
        String pathName = new StringBuilder(getRootDirectory())
                            .append(FILE_DIR)
                            .append(systemFolderPath)
                            .toString();

        File uploadFolder = new File(pathName);

        if (!uploadFolder.exists())
            uploadFolder.mkdirs(); // 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성

        StringBuilder returnedPath = new StringBuilder(FILE_DIR)
                                            .append(formattedDate)
                                            .append(File.separator);

        return returnedPath.toString();
    }

    /**
     * 운영체제에 따른 웹 애플리케이션의 루트 디렉터리 반환
     * @return ex) 톰캣 웹서버 + 리눅스 운영체제 : /var/lib/tomcat/webapps/WebApplication/
     */
    private String getRootDirectory(){
        return request.getSession().getServletContext().getRealPath("/");
    }
}
