package com.stocks.web.rest;

import com.stocks.service.AlertService;
import com.stocks.service.dto.AlertDTO;
import com.stocks.web.rest.errors.BadRequestException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/alert")
public class AlertController {

    private AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @GetMapping("/my")
    public List<AlertDTO> getMyAlerts() {
        return alertService.retrieveMyAlerts();
    }

    @PostMapping
    public AlertDTO createAlert(@RequestBody @Valid AlertDTO alertDTO) {
        return alertService.create(alertDTO);
    }

    @PatchMapping
    public AlertDTO updateAlert(@RequestBody @Valid AlertDTO alertDTO) {
        if (alertDTO.getId() == null) {
            throw new BadRequestException("Missing alert id!");
        }

        return alertService.updateAlert(alertDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteAlert(@PathVariable Long id) {
        alertService.delete(id);
    }

}
