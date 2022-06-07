package com.itboyst.face.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

/**
 * @author st7251
 * @date 2020/3/18 15:45
 */
@Slf4j
@Configuration
public class ArcFaceAutoConfiguration implements InitializingBean, DisposableBean {


    private static final String PLATFORM;

    private static final String USER_HOME;

    public static String CACHE_LIB_FOLDER;

    @Value("${config.arcface-sdk.version}")
    public String ARC_FACE_VERSION;

    static {
        String jvmName = System.getProperty("java.vm.name", "").toLowerCase();
        String osName = System.getProperty("os.name", "").toLowerCase();
        String osArch = System.getProperty("os.arch", "").toLowerCase();
        String abiType = System.getProperty("sun.arch.abi", "").toLowerCase();
        String libPath = System.getProperty("sun.boot.library.path", "").toLowerCase();
        USER_HOME = System.getProperty("user.home");
        if (jvmName.startsWith("dalvik") && osName.startsWith("linux")) {
            osName = "android";
        } else if (jvmName.startsWith("robovm") && osName.startsWith("darwin")) {
            osName = "ios";
            osArch = "arm";
        } else if (osName.startsWith("mac os x") || osName.startsWith("darwin")) {
            osName = "macosx";
        } else {
            int spaceIndex = osName.indexOf(' ');
            if (spaceIndex > 0) {
                osName = osName.substring(0, spaceIndex);
            }
        }
        if (osArch.equals("i386") || osArch.equals("i486") || osArch.equals("i586") || osArch.equals("i686")) {
            osArch = "x86";
        } else if (osArch.equals("amd64") || osArch.equals("x86-64") || osArch.equals("x64")) {
            osArch = "x86_64";
        } else if (osArch.startsWith("aarch64") || osArch.startsWith("armv8") || osArch.startsWith("arm64")) {
            osArch = "arm64";
        } else if ((osArch.startsWith("arm")) && ((abiType.equals("gnueabihf")) || (libPath.contains("openjdk-armhf")))) {
            osArch = "armhf";
        } else if (osArch.startsWith("arm")) {
            osArch = "arm";
        }
        PLATFORM = osName + "-" + osArch;

    }


    @Override
    public void afterPropertiesSet() throws IOException {
        CACHE_LIB_FOLDER = USER_HOME + "/.arcface/cache/" + ARC_FACE_VERSION + "/" + PLATFORM + "/";
        loadLibrary();
    }

    public void loadLibrary() throws IOException {
        String baseFolder = "";
        String suffix = ".dll";
        if ("windows-x86_64".equals(PLATFORM)) {
            baseFolder = "WIN64";
        } else if ("windows-x86".equals(PLATFORM)) {
            baseFolder = "WIN32";
        } else if ("linux-x86_64".equals(PLATFORM)) {
            baseFolder = "LINUX64";
            suffix = ".so";
        }

        if ("".equals(baseFolder)) {
            throw new RuntimeException("ArcFace不支持该操作系统");
        }


        File file = new File(CACHE_LIB_FOLDER);
        if (!file.exists()) {
            file.mkdirs();
        }

        List<String> libList = new LinkedList<>();
        libList.add("libarcsoft_face");
        libList.add("libarcsoft_face_engine");
        libList.add("libarcsoft_face_engine_jni");

        for (String lib : libList) {
            ClassPathResource resource = new ClassPathResource("libs/" + ARC_FACE_VERSION + "/" + baseFolder + "/" + lib + suffix);
            InputStream inputStream = resource.getInputStream();
            int faceLength = inputStream.available();
            File facePath = new File(CACHE_LIB_FOLDER + lib + suffix);
            if (facePath.exists()) {
                if (facePath.length() == faceLength) {
                    continue;
                }
                facePath.delete();
            }
            writeToLocal(CACHE_LIB_FOLDER + lib + suffix, inputStream);
        }
    }

    private void writeToLocal(String destination, InputStream input)
            throws IOException {
        int index;
        byte[] bytes = new byte[1024 * 100];
        FileOutputStream fileOutputStream = new FileOutputStream(destination);
        while ((index = input.read(bytes)) != -1) {
            fileOutputStream.write(bytes, 0, index);
        }
        fileOutputStream.flush();
        fileOutputStream.close();
        input.close();
    }


    @Override
    public void destroy() throws Exception {
    }

}
