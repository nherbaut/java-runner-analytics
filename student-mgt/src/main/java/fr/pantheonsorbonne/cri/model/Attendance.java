package fr.pantheonsorbonne.cri.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public enum Attendance {

    PRESENT,
    ABSENT,
    LATE,
    EXCUSED
}
