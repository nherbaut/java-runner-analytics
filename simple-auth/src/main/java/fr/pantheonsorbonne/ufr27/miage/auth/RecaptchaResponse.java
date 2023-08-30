package fr.pantheonsorbonne.ufr27.miage.auth;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;

public record RecaptchaResponse(
        String name,
        RecaptchaEnterpriseEvent event,
        RecaptchaRiskAnalysis riskAnalysis,
        RecaptchaEntrepriseTokenProperties tokenProperties) {
};



