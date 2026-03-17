package com.homebase.ecom.cconfig.configuration.dao;

import com.homebase.ecom.cconfig.model.Cconfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository  public interface CconfigRepository extends JpaRepository<Cconfig,String> {
    List<Cconfig> findByModuleNameAndCustomAttributeInOrderByKeyNameAscCustomAttributeAsc(String module,List<String> customAttributes);
}
