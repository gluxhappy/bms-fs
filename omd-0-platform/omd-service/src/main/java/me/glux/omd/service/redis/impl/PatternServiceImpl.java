package me.glux.omd.service.redis.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import me.glux.omd.core.LongIdGenerator;
import me.glux.omd.dao.redis.RedisPatternMapper;
import me.glux.omd.model.redis.RedisPattern;
import me.glux.omd.service.redis.PatternService;

@Service
public class PatternServiceImpl implements PatternService {
    @Autowired
    private LongIdGenerator idg;
    
    @Autowired
    private RedisPatternMapper patternMapper;
    
    @Override
    public RedisPattern create(RedisPattern pattern) {
        pattern.setId(idg.next());
        patternMapper.insert(pattern);
        return pattern;
    }

    @Override
    public void delete(Long id) {
        patternMapper.deleteById(id);
    }

    @Override
    public List<RedisPattern> all() {
        return patternMapper.selectAll();
    }

    @Override
    public RedisPattern findById(Long id) {
        return patternMapper.selectById(id);
    }

    @Override
    public void update(RedisPattern pattern) {
        patternMapper.updateById(pattern);
    }

}
