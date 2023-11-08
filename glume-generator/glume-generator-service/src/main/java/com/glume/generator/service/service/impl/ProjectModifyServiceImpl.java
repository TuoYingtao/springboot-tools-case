package com.glume.generator.service.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.ZipUtil;
import com.glume.generator.service.base.service.impl.BaseServiceImpl;
import com.glume.generator.service.domain.entity.ProjectModifyEntity;
import com.glume.generator.service.mapper.ProjectModifyMapper;
import com.glume.generator.service.service.ProjectModifyService;
import com.glume.generator.service.utils.ProjectUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目名变更
 *
 * @Author: TuoYingtao
 * @Date: 2023-09-18 10:46
 * @Version: v1.0.0
 */
@Service
public class ProjectModifyServiceImpl extends BaseServiceImpl<ProjectModifyMapper, ProjectModifyEntity> implements ProjectModifyService {

    @Override
    public void download(HttpServletResponse response, Long id) {
        try {
            ProjectModifyEntity modifyEntity = getBaseMapper().selectById(id);

            // 原项目根路径
            File srcRoot = new File(modifyEntity.getProjectPath());

            // 临时项目根路径
            File destRoot = new File(ProjectUtils.getTmpDirPath(modifyEntity.getModifyProjectName()));

            // 排除的文件
            List<String> exclusions = StrUtil.split(modifyEntity.getExclusions(), ProjectUtils.SPLIT);

            // 获取替换规则
            Map<String, String> replaceMap = getReplaceMap(modifyEntity);

            // 拷贝项目到新路径，并替换路径和文件名
            ProjectUtils.copyDirectory(srcRoot, destRoot, exclusions, replaceMap);

            // 需要替换的文件后缀
            List<String> suffixList = StrUtil.split(modifyEntity.getModifySuffix(), ProjectUtils.SPLIT);

            // 替换文件内容数据
            ProjectUtils.contentFormat(destRoot, suffixList, replaceMap);

            // 生成zip文件
            File zipFile = ZipUtil.zip(destRoot);

            byte[] data = FileUtil.readBytes(zipFile);

            // 清空文件
            FileUtil.clean(destRoot.getParentFile().getParentFile());

            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + modifyEntity.getModifyProjectName() + ".zip\"");
            response.addHeader("Content-Length", "" + data.length);
            response.setContentType("application/octet-stream; charset=UTF-8");

            IoUtil.write(response.getOutputStream(), false, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取替换规则
     */
    private Map<String, String> getReplaceMap(ProjectModifyEntity modifyEntity) {
        Map<String, String> map = new LinkedHashMap<>();

        // 项目路径替换
        String srcPath = "src/main/java/" + modifyEntity.getProjectPackage().replaceAll("\\.", "/");
        String destPath = "src/main/java/" + modifyEntity.getModifyProjectPackage().replaceAll("\\.", "/");
        map.put(srcPath, destPath);

        // 项目包名替换
        map.put(modifyEntity.getProjectPackage(), modifyEntity.getModifyProjectPackage());

        // 项目标识替换
        map.put(modifyEntity.getProjectCode(), modifyEntity.getModifyProjectCode());
        map.put(StrUtil.upperFirst(modifyEntity.getProjectCode()), StrUtil.upperFirst(modifyEntity.getModifyProjectCode()));

        return map;
    }
}
