package com.pccw.interview;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class InfoController {
    @Value("${heartbeat.version}")
    private String version;
    
    @Value("${heartbeat.release.time}")
    private String releaseAt;
    
    @RequestMapping(value="/", method= RequestMethod.GET)
    public String root() {
        return "redirect:/docs";
    }

    @RequestMapping(value="/docs", method= RequestMethod.GET)
    public String showDocs() {
        return "docs";
    }

    @RequestMapping(value="/heartbeat", method= RequestMethod.GET)
    @ResponseBody
    public JSONObject version() {
        JSONObject ret = new JSONObject();
        ret.put("version", version);
        ret.put("releaseAt", releaseAt);
        return ret;
    }
}
