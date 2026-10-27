package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.demo.models.TipoImovelModel;
import com.example.demo.repositories.TipoImovelRepository;

@Service
public class TipoImovelService {

    @Autowired
    private TipoImovelRepository repository;

    public List<TipoImovelModel> getAll() {
        return repository.findAll();
    }

    public Page<TipoImovelModel> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public TipoImovelModel find(Integer id) {
        return repository.findById(id).orElse(null);
    }

    public TipoImovelModel insert(TipoImovelModel model) {
        model.setId(null);
        return repository.save(model);
    }

    public TipoImovelModel update(TipoImovelModel model) {
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
