package com.data.backend.controller;

import com.data.backend.model.Argument;
import com.data.backend.model.dto.DeleteArgumentRequest;
import com.data.backend.model.dto.UpdateArgumentRequest;
import com.data.backend.service.ArgumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PutMapping("/id")
    public ResponseEntity<String> updateArgument(@RequestBody UpdateArgumentRequest request) {
        argumentService.updateArgument(request);
        return ResponseEntity.ok("Argument updated successfully.");
    }

    @DeleteMapping("/id")
    public ResponseEntity<String> deleteArgument(@RequestBody DeleteArgumentRequest request) {
        try {
            // Use the fields from the request object
            argumentService.deleteArgument(request.getArgumentId(), request.getUserId());
            return ResponseEntity.ok("Argument deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred.");
        }
    }
}
