package fr.pantheonsorbonne.ufr27.miage.auth.google;

public record RecaptchaEntrepriseTokenProperties(boolean valid, String hostname, String action, String createTime) {
}
