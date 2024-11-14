package com.data.backend.service;

import com.data.backend.model.Argument;
import com.data.backend.repository.ArgumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArgumentService {

    @Autowired
    private ArgumentRepository argumentRepository;

    public List<Argument> getAllArguments() {
        return argumentRepository.findAll();
    }

    public Optional<Argument> getArgumentById(String id) {
        return argumentRepository.findById(id);
    }

    public Argument createArgument(Argument argument) {
        return argumentRepository.save(argument);
    }

    public Argument updateArgument(String id, Argument argument) {
        argument.setId(id);
        return argumentRepository.save(argument);
    }

    public void deleteArgument(String id) {
        argumentRepository.deleteById(id);
    }
}
