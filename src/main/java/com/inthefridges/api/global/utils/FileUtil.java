package com.inthefridges.api.global.utils;

import com.inthefridges.api.entity.InFridgeFile;

public class FileUtil {
    /**
     * 파일의 경로를 생성 후 반환하는 메서드
     * @param file
     * @return 파일 경로
     */
    public static String getFilePath(InFridgeFile file){
        String extension =  file.getOriginName().substring(file.getOriginName().lastIndexOf("."));
        return file.getPath() + file.getId() + extension;
    }
}
