package ru.noleg.bishopprototype;

import org.springframework.stereotype.Component;
import ru.noleg.synthetichumancorestarter.audit.api.WeylandWatchingYou;

@Component
public class TestService {
    @WeylandWatchingYou
    public String testMethod(String input) {
        return "Hello " + input;
    }
}