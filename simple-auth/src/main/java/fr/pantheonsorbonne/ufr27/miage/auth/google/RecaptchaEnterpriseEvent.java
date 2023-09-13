package fr.pantheonsorbonne.ufr27.miage.auth.google;

public record RecaptchaEnterpriseEvent(String token, String siteKey, String expectedAction) {
}
