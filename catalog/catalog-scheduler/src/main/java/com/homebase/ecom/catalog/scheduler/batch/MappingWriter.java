package com.homebase.ecom.catalog.scheduler.batch;

import com.homebase.ecom.catalog.model.CollectionProductMapping;
import com.homebase.ecom.catalog.repository.CollectionProductMappingRepository;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MappingWriter implements ItemWriter<List<CollectionProductMapping>> {

    @Autowired
    private CollectionProductMappingRepository repository;

    @Override
    public void write(Chunk<? extends List<CollectionProductMapping>> chunk) throws Exception {
        for (List<CollectionProductMapping> items : chunk) {
            if (!items.isEmpty()) {
                repository.saveAll(items);
            }
        }
    }
}
