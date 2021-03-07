package com.example.democamel.service;

import com.example.democamel.dao.DummyDao;
import com.example.democamel.model.Dummy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.logging.Logger;

@Service("userService")
public class DummyServiceImpl implements DummyService {

    Logger logger = Logger.getLogger(DummyServiceImpl.class.getName());

    @Autowired
    private DummyDao dummyDao;

    @Override
    public Dummy findUser(Integer id) {
        Optional<Dummy> optDummy = dummyDao.findById(id);
        return optDummy.isPresent()?optDummy.get():null;
    }

    @Override
    public Collection<Dummy> findUsers() {
        return dummyDao.findAll();
    }

    @Override
    public void updateUser(Dummy dummy) {
        dummyDao.save(dummy);
    }

    @Override
    public Dummy create(Dummy dummy) {
        return dummyDao.save(dummy);
    }

    @Override
    public void delete(Integer id) {
        Optional<Dummy> optDumm = dummyDao.findById(id);
        if (optDumm.isPresent()) dummyDao.delete(optDumm.get());
    }

    @Override
    public long count() {
        return dummyDao.count();
    }
}
