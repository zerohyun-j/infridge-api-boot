package com.inthefridges.api.service;

import com.inthefridges.api.dto.request.FileRequest;
import com.inthefridges.api.dto.response.FileResponse;
import com.inthefridges.api.entity.InFridgeFile;
import com.inthefridges.api.entity.Member;
import com.inthefridges.api.global.exception.ExceptionCode;
import com.inthefridges.api.global.exception.ServiceException;
import com.inthefridges.api.repository.FileRepository;
import com.inthefridges.api.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    @Value("${file.dir}")
    private String FILE_DIR;
    private final HttpServletRequest request;

    @Override
    public FileResponse get(Long memberId, FileRequest fileRequest) {
        InFridgeFile file = convertToFileEntity(memberId, fileRequest);

        // 삭제하려는 회원과 삭제 파일 회원 검증
        Member member = fetchMemberById(memberId);
        InFridgeFile fetchFile = fetchFileByIdAndPostId(file);
        validateMemberFileMatch(member, fetchFile);

        return convertToFileResponse(fetchFile);
    }

    @Override
    public InFridgeFile getProfileImageAndUpdate(InFridgeFile file) {
        InFridgeFile fetchFile = repository.findByMemberId(file.getMemberId())
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));

        // TODO : 리팩토링
        // OAuth Profile Path
        if(!fetchFile.getOriginName().equals(fetchFile.getPath()))
            // 현재 profileImage != 기존 profileImage
            if (!file.getOriginName().equals(fetchFile.getOriginName())) {
                repository.delete(fetchFile);
                repository.save(file);
                return file;
            }

        return fetchFile;
    }

    @Override
    public FileResponse create(Long memberId, MultipartFile requestFile) {

        if(requestFile.isEmpty())
            throw new ServiceException(ExceptionCode.MISSING_DATA);

        // resources/dev/2023/09와 같은 path 반환
        String dateFolderPath = createOrGetFolderPathByDate();

        // 클라이언트가 업로드한 파일 이름 추출
        String originalFileName = requestFile.getOriginalFilename();

        // DB 저장
        InFridgeFile newFile = InFridgeFile.builder()
                .path(dateFolderPath)
                .originName(originalFileName)
                .memberId(memberId)
                .build();
        repository.save(newFile);

        String newFileId = String.valueOf(newFile.getId());

        try {
            // 확장자 추출
            String fileExtension = getFileExtension(originalFileName);
            // 실제 파일이 저장 될 경로
            StringBuilder saveDirectoryPath = new StringBuilder(getRootDirectory())
                    .append(dateFolderPath);

            String fullPath = saveDirectoryPath.append(newFileId).append(fileExtension).toString();
            File destination = new File(fullPath);
            requestFile.transferTo(destination);
        } catch (IOException e) {
            throw new ServiceException(ExceptionCode.INTERNAL_SERVER_ERROR);
        }

        return new FileResponse(newFile.getId(), dateFolderPath, originalFileName);
    }

    @Override
    public FileResponse update(Long memberId, FileRequest fileRequest) {
        InFridgeFile file = convertToFileEntity(memberId, fileRequest);

        // 삭제하려는 회원과 삭제 파일 회원 검증
        Member member = fetchMemberById(memberId);
        InFridgeFile findFile = repository.findById(file)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));
        validateMemberFileMatch(member, findFile);

        if(fileRequest.fridgeId() != null)
            findFile.setFridgeId(fileRequest.fridgeId());
        else
            findFile.setItemId(fileRequest.itemId());

        repository.update(findFile);
        InFridgeFile updatedFile = fetchFileByIdAndPostId(findFile);

        return convertToFileResponse(updatedFile);
    }

    @Override
    public void delete(Long memberId, FileRequest fileRequest) {
        InFridgeFile file = convertToFileEntity(memberId, fileRequest);

        // 삭제하려는 회원과 삭제 파일 회원 검증
        Member member = fetchMemberById(memberId);
        InFridgeFile findFile = fetchFileByIdAndPostId(file);
        validateMemberFileMatch(member, findFile);

        repository.delete(findFile);
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

    private String getFileExtension(String fileName){
        return fileName.substring(fileName.lastIndexOf("."));
    }

    private InFridgeFile fetchFileByIdAndPostId(InFridgeFile file){
        return repository.findByIdAndPostId(file)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_FILE));
    }

    /**
     * memberId 로 member 찾기
     * @param memberId principal's memberId
     * @return Member
     */
    private Member fetchMemberById(Long memberId) {
        return memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ServiceException(ExceptionCode.NOT_FOUND_MEMBER));
    }

    /**
     * principal memberId 와 접근 파일의 작성자 memberId 가 같은지 비교
     * @param member principal's memberId
     * @param file 접근하고자하는 파일
     */
    private void validateMemberFileMatch(Member member, InFridgeFile file) {
        if (!file.getMemberId().equals(member.getId()))
            throw new ServiceException(ExceptionCode.NOT_MATCH_MEMBER);
    }

    /**
     * FileRequest -> InFridgeFile Entity
     */
    private InFridgeFile convertToFileEntity(Long memberId, FileRequest fileRequest){
        return InFridgeFile.builder()
                .memberId(memberId)
                .id(fileRequest.id())
                .fridgeId(fileRequest.fridgeId())
                .itemId(fileRequest.itemId())
                .build();
    }

    /**
     * InFridgeFile Entity -> FileRequest
     */
    private FileResponse convertToFileResponse(InFridgeFile file){
        String fullPath = new StringBuilder("/")
                .append(file.getPath())
                .append(file.getId())
                .append(getFileExtension(file.getOriginName()))
                .toString();
        return new FileResponse(file.getId(), fullPath, file.getOriginName());
    }
}
