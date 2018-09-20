package org.ruoxue.backend.controller;

import org.ruoxue.backend.common.controller.BaseController;
import org.ruoxue.backend.util.Base64Util;
import org.ruoxue.backend.util.IOUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;

@Controller
public class ResourceController extends BaseController {

    public static String distDir = System.getProperty("user.dir") + "/src/main/resources/static/dist/";

    public void responseBinary(String path, String file, HttpServletResponse response) {
        if (file.contains("..") && (file.contains("/") || file.contains("\\"))) {
            response.setStatus(403);
            return;
        }
        file = path + file;
        response.setHeader("X-Resource-Mapped-By", "ResourceController");
        //response.setHeader("X-Actual-Path", distDir + "/" + path + file);
        try {
            byte[] bytes = IOUtil.read(file);
            response.getOutputStream().write(bytes);
        }
        catch (Exception err) {
            response.setStatus(404);
        }
    }

    @GetMapping("/res/avatar/{file:[\\w\\d\\.]+}")
    public void staticResAvatar(@PathVariable String file, HttpServletResponse response) {
        if (Base64Util.baseImageSavePath == null) {
            String savePath=System.getProperty("user.dir") + "/src/main/resources/static/uploads/";
            Base64Util.baseImageSavePath = savePath;
        }
        responseBinary(Base64Util.baseImageSavePath, file, response);
    }

    @GetMapping("/dist/{file:[\\w\\d\\.]+}")
    public void staticDistriutebDirectory(@PathVariable String file, HttpServletResponse response) {
        response.setHeader("Cache-Control", "max-age=31536000");
        responseBinary(distDir, file, response);
    }
}
