package com.homebase.ecom.policy.service.configuration.controller;

import com.homebase.ecom.policy.api.dto.PolicyDto;
import com.homebase.ecom.policy.domain.model.Policy;
import com.homebase.ecom.policy.infrastructure.mapper.PolicyDtoMapper;
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
import com.homebase.ecom.policy.domain.repository.PolicyRepository;

@RestController
@ChenileController(value = "policyService", serviceName = "policyStateEntityService", healthCheckerName = "policyHealthChecker")
public class PolicyController extends ControllerSupport {

    @Autowired
    private PolicyDtoMapper policyDtoMapper;

    @Autowired
    private PolicyRepository policyRepository;



    @PostMapping("/policy")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<PolicyDto>>> create(
            HttpServletRequest httpServletRequest,
            @ChenileParamType(StateEntity.class) @RequestBody Policy entity) {
        ResponseEntity<GenericResponse<StateEntityServiceResponse<Policy>>> response = process(httpServletRequest, entity);
        return transform(response);
    }

    @PatchMapping("/policy/{id}/{eventID}")
    @BodyTypeSelector("policyBodyTypeSelector")
    public ResponseEntity<GenericResponse<StateEntityServiceResponse<PolicyDto>>> processById(
            HttpServletRequest httpServletRequest,
            @PathVariable String id,
            @PathVariable String eventID,
            @ChenileParamType(Object.class) @RequestBody Object eventPayload) {
        ResponseEntity<GenericResponse<StateEntityServiceResponse<Policy>>> response = process(httpServletRequest, id, eventID, eventPayload);
        return transform(response);
    }

    private ResponseEntity<GenericResponse<StateEntityServiceResponse<PolicyDto>>> transform(
            ResponseEntity<GenericResponse<StateEntityServiceResponse<Policy>>> response) {
        GenericResponse<StateEntityServiceResponse<Policy>> body = response.getBody();
        if (body == null || body.getData() == null) {
            return ResponseEntity.status(response.getStatusCode()).build();
        }

        StateEntityServiceResponse<Policy> sesr = body.getData();
        PolicyDto policyDto = policyDtoMapper.toDto(sesr.getMutatedEntity());

        StateEntityServiceResponse<PolicyDto> sesrDto = new StateEntityServiceResponse<>();
        sesrDto.setMutatedEntity(policyDto);
        sesrDto.setAllowedActionsAndMetadata(sesr.getAllowedActionsAndMetadata());

        GenericResponse<StateEntityServiceResponse<PolicyDto>> genericResponseDto = new GenericResponse<>(sesrDto);
        genericResponseDto.setSuccess(body.isSuccess());
        genericResponseDto.setCode(body.getCode());

        if (body.getErrors() != null) {
            body.getErrors().forEach(genericResponseDto::addWarningMessage);
        }

        return new ResponseEntity<>(genericResponseDto, response.getHeaders(), response.getStatusCode());
    }
}
