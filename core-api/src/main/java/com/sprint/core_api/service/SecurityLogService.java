package com.sprint.core_api.service;

import com.sprint.core_api.dto.request.CreateLogRequest;
import com.sprint.core_api.entity.SecurityLog;
import com.sprint.core_api.repository.SecurityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class SecurityLogService {

    private final SecurityLogRepository securityLogRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void saveLog(String username, String action, String ip, String status, String details) {
        SecurityLog logEntry = new SecurityLog(new CreateLogRequest(
                username,
                action,
                ip,
                status,
                details
        ));
        securityLogRepository.save(logEntry);
    }
}