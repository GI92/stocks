package com.stocks.service.mapper;

import com.stocks.domain.Alert;
import com.stocks.service.dto.AlertDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlertMapperTest {

    private AlertMapper alertMapper;

    @BeforeEach
    public void init() {
        alertMapper = new AlertMapper();
    }

    @Test
    public void alertFromDTO() {
        AlertDTO alertDTO = new AlertDTO();
        alertDTO.setPrice(10.0);
        alertDTO.setPercentage(20);
        alertDTO.setStock("IBM");

        final Alert alert = alertMapper.fromDTO(alertDTO);

        assertThat(alert).isNotNull();
        assertThat(alert.isActive()).isTrue();
        assertThat(alert.getPrice()).isEqualTo(alertDTO.getPrice());
        assertThat(alert.getPercentage()).isEqualTo(alertDTO.getPercentage());
        assertThat(alert.getStock()).isEqualTo(alertDTO.getStock());
    }

    @Test
    public void alertToDTO() {
        Alert alert = new Alert();
        alert.setActive(true);
        alert.setId(10L);
        alert.setPercentage(-20);
        alert.setPrice(115.5);
        alert.setStock("IBM");

        final AlertDTO alertDTO = alertMapper.toDTO(alert);

        assertThat(alertDTO).isNotNull();
        assertThat(alertDTO.isActive()).isTrue();
        assertThat(alertDTO.getPrice()).isEqualTo(alert.getPrice());
        assertThat(alertDTO.getPercentage()).isEqualTo(alert.getPercentage());
        assertThat(alertDTO.getStock()).isEqualTo(alert.getStock());
        assertThat(alertDTO.getId()).isEqualTo(alert.getId());
    }
}