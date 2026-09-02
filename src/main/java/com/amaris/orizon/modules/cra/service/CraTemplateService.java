package com.amaris.orizon.modules.cra.service;

import com.amaris.orizon.modules.cra.dto.CraTemplateResponse;
import com.amaris.orizon.modules.cra.dto.CraTemplateSaveRequest;

import java.util.List;

public interface CraTemplateService {
    CraTemplateResponse saveTemplate(CraTemplateSaveRequest request);
    List<CraTemplateResponse> getUserTemplates(Long userId);
    void deleteTemplate(Long id);
}
