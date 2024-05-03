package com.example.assignment.CommonUtils;

import com.example.assignment.Enums.ExceptionList;
import com.example.assignment.Exception.BusinessException;
import com.example.assignment.Model.AppointmentRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestUtils {
    public void validateAppointmentRequest(AppointmentRequest appointMentRequest) throws BusinessException {
        try {
            if(appointMentRequest.getOperatorId()<=0 || appointMentRequest.getOperatorId()>4
             || appointMentRequest.getDay()<=0 || appointMentRequest.getDay()>7
             || appointMentRequest.getStart_time()<=0 || appointMentRequest.getStart_time()>24)
                throw new BusinessException(ExceptionList.INVALID_REQUEST.getId(), ExceptionList.INVALID_REQUEST.getText());
        }
        catch(BusinessException e) {
            throw  new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
    public void validateOPerator(int operatorId) throws BusinessException {
        try {
            if(operatorId <0  || operatorId>3) {
                throw new BusinessException(ExceptionList.INVALID_REQUEST.getId(), ExceptionList.INVALID_REQUEST.getText());
            }
        }
        catch(BusinessException e) {
            throw  new BusinessException(e.getId(),e.getMessage());
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }
}
