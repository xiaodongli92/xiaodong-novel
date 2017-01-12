package com.xiaodong.novel.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by xiaodong on 2017/1/12.
 * 健康检查
 */
@Controller
@RequestMapping("health")
public class HealthCheckController {

    @RequestMapping("check.do")
    @ResponseBody
    public String check() {
        return "ok";
    }
}
