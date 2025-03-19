package com.security.securityImpl.config;

import com.security.securityImpl.entity.csv.IndustryData;
import org.springframework.batch.item.ItemProcessor;


public class IndustryDataProcessor implements ItemProcessor<IndustryData, IndustryData> {
    @Override
    public IndustryData process(IndustryData item) throws Exception {

        return item;
    }
}
