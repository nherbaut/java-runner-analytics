package fr.pantheonsorbonne.ufr27.miage.auth;

public record RecaptchaEntrepriseTokenProperties(boolean valid, String hostname, String action, String createTime) {
}
