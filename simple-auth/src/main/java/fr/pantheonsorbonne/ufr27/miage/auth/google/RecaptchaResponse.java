package fr.pantheonsorbonne.ufr27.miage.auth.google;

public record RecaptchaResponse(
        String name,
        RecaptchaEnterpriseEvent event,
        RecaptchaRiskAnalysis riskAnalysis,
        RecaptchaEntrepriseTokenProperties tokenProperties) {
};



