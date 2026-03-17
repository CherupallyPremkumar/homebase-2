package com.homebase.ecom.rulesengine.configuration.controller;

import com.homebase.ecom.rulesengine.api.dto.RuleSetDto;
import com.homebase.ecom.rulesengine.domain.model.RuleSet;
import com.homebase.ecom.rulesengine.infrastructure.mapper.RuleSetDtoMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.chenile.base.response.GenericResponse;
import org.chenile.http.annotation.BodyTypeSelector;
import org.chenile.http.annotation.ChenileController;
import org.chenile.http.annotation.ChenileParamType;
import org.chenile.http.handler.ControllerSupport;
import org.chenile.stm.StateEntity;
import org.chenile.workflow.dto.StateEntityServiceResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.homebase.ecom.rulesengine.domain.repository.RuleSetRepository;

@RestController
@ChenileController(value = "ruleSetService", serviceName = "ruleSetStateEntityService", healthCheckerName = "ruleSetHealthChecker")
@Tag(name = "RuleSet", description = "Business rules engine")
public class RuleSetController extends ControllerSupport {

    @Autowired
    private RuleSetDtoMapper ruleSetDtoMapper;

    @Autowired
    private RuleSetRepository ruleSetRepository;



    @PostMapping("/ruleSet")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSetDto>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody RuleSet entity) {
        ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> response = process(httpServletRequest, entity);
        return transform(response);
    }

    @PatchMapping("/ruleSet/{id}/{eventID}")
    @BodyTypeSelector("ruleSetBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSetDto>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody Object eventPayload) {
        ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> response = process(httpServletRequest, id, eventID, eventPayload);
        return transform(response);
    }

    private ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSetDto>>> transform(
            ResponseEntity<GenericResponse<StateEntityServiceResponse<RuleSet>>> response) {
        GenericResponse<StateEntityServiceResponse<RuleSet>> body = response.getBody();
        if (body == null || body.getData() == null) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }

        StateEntityServiceResponse<RuleSet> sesr = body.getData();
        RuleSetDto ruleSetDto = ruleSetDtoMapper.toDto(sesr.getMutatedEntity());

        StateEntityServiceResponse<RuleSetDto> sesrDto = new StateEntityServiceResponse<>();
        sesrDto.setMutatedEntity(ruleSetDto);
        sesrDto.setAllowedActionsAndMetadata(sesr.getAllowedActionsAndMetadata());

        GenericResponse<StateEntityServiceResponse<RuleSetDto>> genericResponseDto = new GenericResponse<>(sesrDto);
        genericResponseDto.setSuccess(body.isSuccess());
        genericResponseDto.setCode(body.getCode());

        if (body.getErrors() != null) {
            body.getErrors().forEach(genericResponseDto::addWarningMessage);
        }

        return new ResponseEntity<>(genericResponseDto, response.getHeaders(), response.getStatusCode());
    }
}
