package com.hdse242052.lms_final.service;

import com.hdse242052.lms_final.entity.Result;
import com.hdse242052.lms_final.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository resultRepository;

    public Result saveResult(Result result) {
        return resultRepository.save(result);
    }

    public List<Result> getResultsByIndex(String indexNumber) {
        return resultRepository.findByIndexNumber(indexNumber);
    }
}