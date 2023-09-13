package fr.pantheonsorbonne.ufr27.miage.auth.discord;

import jakarta.enterprise.context.ApplicationScoped;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@ApplicationScoped
public class StateManager {

    BlockingQueue<String> states = new ArrayBlockingQueue<>(100);

    public void pushState(String state) {
        states.offer(state);
    }

    public boolean popState(String state) {
        return states.remove(state);
    }


}
