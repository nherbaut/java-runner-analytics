package fr.pantheonsorbonne.ufr27.miage.auth;

public record RecaptchaEnterpriseEvent(String token, String siteKey, String expectedAction) {
}
