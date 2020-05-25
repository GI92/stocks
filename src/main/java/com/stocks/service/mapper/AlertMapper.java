package com.stocks.service.mapper;

import com.stocks.domain.Alert;
import com.stocks.service.dto.AlertDTO;
import org.springframework.stereotype.Component;

@Component
public class AlertMapper implements DTOMapper<AlertDTO, Alert> {

    @Override
    public Alert fromDTO(AlertDTO alertDTO) {
        Alert alert = new Alert();

        alert.setActive(true);
        alert.setPrice(alertDTO.getPrice());
        alert.setPercentage(alertDTO.getPercentage());
        alert.setStock(alertDTO.getStock());

        return alert;
    }

    @Override
    public AlertDTO toDTO(Alert alert) {
        AlertDTO alertDTO = new AlertDTO();

        alertDTO.setActive(alert.isActive());
        alertDTO.setId(alert.getId());
        alertDTO.setPercentage(alert.getPercentage());
        alertDTO.setPrice(alert.getPrice());
        alertDTO.setStock(alert.getStock());

        return alertDTO;
    }
}
