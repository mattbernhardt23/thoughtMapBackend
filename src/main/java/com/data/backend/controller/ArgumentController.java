package com.data.backend.controller;

import com.data.backend.model.Argument;
import com.data.backend.service.ArgumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/arguments")
public class ArgumentController {

    @Autowired
    private ArgumentService argumentService;

    @GetMapping
    public List<Argument> getAllArguments() {
        return argumentService.getAllArguments();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Argument> getArgumentById(@PathVariable String id) {
        Optional<Argument> argument = argumentService.getArgumentById(id);
        return argument.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Argument createArgument(@RequestBody Argument argument) {
        return argumentService.createArgument(argument);
    }

    @PostMapping("/bulk")
    public List<Argument> createArguments(@RequestBody List<Map<String, Object>> input) {
        return argumentService.createArguments(input);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Argument> updateArgument(@PathVariable String id, @RequestBody Argument argument) {
        Argument updatedArgument = argumentService.updateArgument(id, argument);
        return ResponseEntity.ok(updatedArgument);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArgument(@PathVariable String id) {
        argumentService.deleteArgument(id);
        return ResponseEntity.noContent().build();
    }
}
