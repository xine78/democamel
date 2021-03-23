package com.example.democamel.service;

import com.example.democamel.dao.DummyDao;
import com.example.democamel.model.Dummy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
//import java.util.logging.Logger;

@Service("dummyService")
@Slf4j
public class DummyServiceImpl implements DummyService {

    //Logger logger = Logger.getLogger(DummyServiceImpl.class.getName());

    @Autowired
    private DummyDao dummyDao;

    @Override
    public Dummy findDummy(Integer id) {
        Optional<Dummy> optDummy = dummyDao.findById(id);
        return optDummy.orElse(null);
    }

    @Override
    public Collection<Dummy> findDummys() {
        return dummyDao.findAll();
    }

    @Override
    public void updateDummy(Integer id, Dummy dummy) {
        Optional<Dummy> optDumm = dummyDao.findById(id);
        if (optDumm.isPresent()) dummyDao.save(dummy);
    }

    @Override
    public Dummy create(Dummy dummy) {
        log.info(">>> Created Dummy: "+dummy.getName());
        return dummyDao.save(dummy);
    }

    @Override
    public void delete(Integer id) {
        Optional<Dummy> optDumm = dummyDao.findById(id);
        optDumm.ifPresent(dummy -> dummyDao.delete(dummy));
    }

    @Override
    public long count() {
        return dummyDao.count();
    }
}
