package ${package}.base.domain.query;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 查询 基类
 *
 * @Author: ${author}
 * @Date: ${datetime}
 * @Version: v${version}
 */
@Data
public class BaseParamQuery implements Serializable {
    private static final long serialVersionUID = 6384218674179815390L;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date beginTime;
    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endTime;
    /**
     * 页码
     */
    Integer pageNum;
    /**
     * 每页条数
     */
    Integer limit;
}
