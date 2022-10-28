package com.tuoyingtao.easypoiexceltools.controller;

import com.tuoyingtao.easypoiexceltools.common.api.R;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tuoyingtao
 * @create 2022-10-28 18:04
 */
@RestController
@RequestMapping("/easypoi/")
public class EasyPoiController {

    @RequestMapping(path = "test", method = RequestMethod.GET)
    public R easyPoiTest() {
        return R.ok().put("data","easyPoiTest");
    }
}
