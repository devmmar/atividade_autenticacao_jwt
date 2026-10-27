package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.models.BairroModel;
import com.example.demo.repositories.BairroRepository;

@Service
public class BairroService {

    @Autowired
    private BairroRepository repository;

    public List<BairroModel> getAll() {
        return repository.findAll();
    }

    public Page<BairroModel> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public BairroModel find(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public BairroModel insert(BairroModel model) {
        model.setId(null);
        return repository.save(model);
    }

    public BairroModel update(BairroModel model) {
        if (!repository.existsById(model.getId())) {
            return null;
        }
        return repository.save(model);
    }

    public boolean delete(Integer id) {
        if (!repository.existsById(id)) {
            return false;
        }
        repository.deleteById(id);
        return true;
    }
}
