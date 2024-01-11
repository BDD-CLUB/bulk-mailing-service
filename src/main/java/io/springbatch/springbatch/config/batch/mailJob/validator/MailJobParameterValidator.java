package io.springbatch.springbatch.config.batch.mailJob.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;

@Component
public class MailJobParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        isParameterValid(parameters, "mailSubject");
        isParameterValid(parameters, "mailMessage");
    }

    private void isParameterValid(JobParameters parameters, String requiredKey) throws JobParametersInvalidException {
        if (parameters.getString(requiredKey) == null) {
            throw new JobParametersInvalidException("Mail Job의 필수 파라미터 값인 " + requiredKey + "가 없습니다");
        }
    }
}
