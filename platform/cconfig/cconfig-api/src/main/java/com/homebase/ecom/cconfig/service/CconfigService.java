package com.homebase.ecom.cconfig.service;

import com.homebase.ecom.cconfig.model.Cconfig;

public interface CconfigService {
    public Cconfig save(Cconfig cconfig);
    public Cconfig retrieve(String id);
}
