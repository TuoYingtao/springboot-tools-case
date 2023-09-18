package com.glume.generator.service.controller;

import cn.hutool.core.io.IoUtil;
import com.glume.generator.framework.commons.Result;
import com.glume.generator.service.service.GeneratorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 13:58
 * @Version: v1.0.0
 */
@Controller
@RequestMapping("generator/generator")
public class GeneratorController {

    @Autowired
    private GeneratorService generatorService;

    /**
     * 生成代码（自定义目录）
     */
    @ResponseBody
    @PostMapping("code")
    public Result<String> code(@RequestBody Long[] tableIds) throws Exception {
        generatorService.generatorCode(tableIds);

        return Result.ok("成功生成代码");
    }

    /**
     * 生成代码（zip压缩包）
     */
    @GetMapping("download")
    public void download(String tableIds, HttpServletResponse response) throws Exception {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);

        // 生成代码
        for (String tableId : tableIds.split(",")) {
            generatorService.downloadCode(Long.parseLong(tableId), zip);
        }

        IoUtil.close(zip);

        // zip压缩包数据
        byte[] data = outputStream.toByteArray();

        response.reset();
        response.setHeader("Content-Disposition", "attachment; filename=\"glume.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");

        IoUtil.write(response.getOutputStream(), false, data);
    }
}
