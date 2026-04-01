package com.homebase.ecom.cconfig.service.impl;

import com.homebase.ecom.cconfig.configuration.dao.CconfigRepository;
import com.homebase.ecom.cconfig.model.Cconfig;
import com.homebase.ecom.cconfig.service.CconfigRetriever;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class DbBasedCconfigRetriever implements CconfigRetriever {
    @Autowired
    CconfigRepository cconfigRepository;

    @Override
    public List<Cconfig> findAllKeysForModule(String module, String customAttribute) {
        List<String> customAttributes = new ArrayList<>();
        customAttributes.add(Cconfig.GLOBAL_CUSTOMIZATION_ATTRIBUTE);
        customAttributes.add(customAttribute);
        return cconfigRepository.findByModuleNameAndCustomAttributeInOrderByKeyNameAscCustomAttributeAsc(module,customAttributes);
    }
}
