package me.glux.omd.controller.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import me.glux.omd.dto.enums.Result;
import me.glux.omd.dto.redis.KeySummaryDto;
import me.glux.omd.model.redis.RedisKey;
import me.glux.omd.model.redis.RedisPattern;
import me.glux.omd.rest.anno.JsonApi;
import me.glux.omd.rest.anno.JsonParam;
import me.glux.omd.security.OmdSecurityContextHolder;
import me.glux.omd.service.redis.PatternService;
import me.glux.omd.service.redis.RedisService;

@Controller
@RequestMapping("/redis")
public class RedisController {
    private static final Logger logger=LoggerFactory.getLogger(RedisController.class);

    @Autowired
    private RedisService redis;
    
    @Autowired
    private PatternService patternService;

    @RequestMapping("/key/search")
    public String patterns(
            @RequestParam(value="pattern",required=false) String pattern, 
            @RequestParam(value="db",required=false) Integer db, 
            Model model) {
        if (!StringUtils.isBlank(pattern) && db != null) {
            model.addAttribute("pattern", pattern);
            model.addAttribute("db", db);
            Set<String> keyStrings=redis.search(db, pattern);
            List<KeySummaryDto> keys=new ArrayList<>(keyStrings.size());
            for(String keyString:keyStrings){
                keys.add(new KeySummaryDto(keyString, db));
            }
            model.addAttribute("keys", keys);
        }
        return "redis/key/search";
    }
    

    @RequestMapping("/pattern/list")
    public String patternList(Model model) {
        model.addAttribute("patterns", patternService.all());
        return "redis/pattern/list";
    }   

    @RequestMapping("/pattern/create.do")
    public String doPatternCreate(
            @RequestParam("pattern") String pattern,
            @RequestParam("name") String name,
            @RequestParam("db") Integer db,
            @RequestParam("desp") String desp,
            Model model) {
        RedisPattern patte=new RedisPattern();
        patte.setDb(db);
        patte.setDesp(desp);
        patte.setName(name);
        patte.setPattern(pattern);
        patte.setCreator(OmdSecurityContextHolder.getUserDetails().getUsername());
        patternService.create(patte);
        model.addAttribute("patterns", patternService.all());
        return "redis/pattern/list";
    }
    

    @RequestMapping("/pattern/update.do")
    public String doPatternUpdate(
            @RequestParam("id") Long id,
            @RequestParam("pattern") String pattern,
            @RequestParam("name") String name,
            @RequestParam("db") Integer db,
            @RequestParam("desp") String desp,
            Model model) {

        RedisPattern patte=new RedisPattern();
        patte.setId(id);
        patte.setDb(db);
        patte.setDesp(desp);
        patte.setName(name);
        patte.setPattern(pattern);
        patte.setCreator(OmdSecurityContextHolder.getUserDetails().getUsername());
        patternService.update(patte);
        model.addAttribute("patterns", patternService.all());
        return "redis/pattern/list";
    }
    
    @RequestMapping("/pattern/detail")
    public String patternDetail(@RequestParam(value="id",required=false) Long id,Model model) {
        if(null != id){
            model.addAttribute("pattern", patternService.findById(id));
        }
        return "redis/pattern/detail";
    }
    
    @RequestMapping("/pattern/delete.do")
    @JsonApi
    public Result doDelete(@JsonParam("id") Long id) {
        patternService.delete(id);
        return Result.SUCCESS;
    }
    
    
    @RequestMapping("/key/detail.do")
    @JsonApi
    public RedisKey doDetail(@JsonParam("key") String key,@JsonParam("db") Integer db) {
        return redis.detail(db, key);
    }
    
    @RequestMapping("/info")
    public String info(Model model) {
        model.addAttribute("info", redis.info());
        return "redis/info";
    }
    
    @RequestMapping("/flush")
    public String flush(@RequestParam(value="db",required=false,defaultValue="-2") int db,Model model) {
        if(db >= -1){
            if(db == -1){
                redis.flushAll();
            }else{
                redis.flush(db);
            }
            model.addAttribute("db", db);
        }
        return "redis/flush";
    }
    
    @RequestMapping("/menu")
    public String menu(Model model) {
        model.addAttribute("name", "admin/menu");
        return "redis/menu";
    }
    
    @RequestMapping("/key/delete.do")
    @JsonApi
    public Result doDelete(@JsonParam("key") String key,@JsonParam("db") int db) {
        try{
            redis.delete(db, key);
            return Result.SUCCESS;
        }catch(Exception e){
            logger.warn("Error",e);
            return Result.ERROR;
        }
    }
    
}
