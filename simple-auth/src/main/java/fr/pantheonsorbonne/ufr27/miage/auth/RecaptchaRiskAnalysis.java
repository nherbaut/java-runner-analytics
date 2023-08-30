package fr.pantheonsorbonne.ufr27.miage.auth;

import java.util.Collection;

public record RecaptchaRiskAnalysis(String score, Collection<String> reasons) {
}
