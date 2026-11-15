package com.plazoleta.domain.api;

import com.plazoleta.domain.model.EfficiencyReport;

public interface IOrderEfficiencyServicePort {
    EfficiencyReport getEfficiency(Long restaurantId);   // HU18
}