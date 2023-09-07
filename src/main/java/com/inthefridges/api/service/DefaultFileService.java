package com.inthefridges.api.service;

import com.inthefridges.api.entity.InFridgeFile;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.FileRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class DefaultFileService implements FileService{

    private final FileRepository repository;
    @Value("${file.dir}")
    private final String FILE_DIR;
    private HttpServletRequest request;

    @Override
    public Long create(MultipartFile requestFile) {

        if(requestFile.isEmpty())
            throw new ServiceException(ExceptionCode.MISSING_DATA);

        // /var/lib/tomcat/webapps/WebApplication/resources/dev/2023/09와 같은 path 반환
        String savePath = createOrGetFolderPathByDate();

        // 클라이언트가 업로드한 파일 이름 추출
        String originName = requestFile.getOriginalFilename();

        // 확장자 추출
        String extension = originName.substring(originName.lastIndexOf("."));

        // DB 저장
        InFridgeFile file = InFridgeFile.builder()
                .path(savePath)
                .originName(originName)
                .build();
        repository.save(file);

        String fileId = String.valueOf(file.getId());
        try {
            requestFile.transferTo(new File(String.join(savePath.replace("\\", "/"), fileId, extension)));
        } catch (IOException e) {
            throw new ServiceException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }

        return file.getId();
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
        String pathName = String.join(getRootDirectory(), FILE_DIR, systemFolderPath);

        File uploadFolder = new File(pathName);

        if (!uploadFolder.exists())
            uploadFolder.mkdirs(); // 상위 디렉토리가 존재하지 않을 경우에는 상위 디렉토리까지 모두 생성

        return String.join(pathName, File.separator);
    }

    /**
     * 운영체제에 따른 웹 애플리케이션의 루트 디렉터리 반환
     * @return ex) 톰캣 웹서버 + 리눅스 운영체제 : /var/lib/tomcat/webapps/WebApplication/
     */
    private String getRootDirectory(){
        return request.getSession().getServletContext().getRealPath("/");
    }
}
