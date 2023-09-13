package fr.pantheonsorbonne.ufr27.miage.auth.google;

import java.util.Collection;

public record RecaptchaRiskAnalysis(String score, Collection<String> reasons) {
}
