package com.simonian.web;

import com.simonian.dto.TransactionDTO;
import com.simonian.model.Transaction;
import com.simonian.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public List<Transaction> transactions() {
        return transactionService.findAll();
    }

    @PostMapping("/transactions")
    public Transaction create(@RequestBody @Valid TransactionDTO transactionDTO) {
        return transactionService.create(transactionDTO.getAmount(), transactionDTO.getReference(), transactionDTO.getReceivingUser());
    }
}
