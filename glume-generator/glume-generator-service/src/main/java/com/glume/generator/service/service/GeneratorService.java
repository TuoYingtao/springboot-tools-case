package com.glume.generator.service.service;

import java.util.zip.ZipOutputStream;

/**
 * 代码生成
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 13:58
 * @Version: v1.0.0
 */
public interface GeneratorService {

    void generatorCode(Long[] tableIds);

    void downloadCode(long tableId, ZipOutputStream zip);
}
