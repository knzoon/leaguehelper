package se.knzoon.leaguehelper.service;

import org.springframework.stereotype.Component;
import se.knzoon.leaguehelper.model.Takeover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class RevisitChainExtractor {

    public List<List<Takeover>> extractChains(List<Takeover> takeovers) {
        List<List<Takeover>> chains = new ArrayList<>();
        Long currentUserId = 0L;
        List<Takeover> currentChain = List.of();

        for (Takeover takeover : takeovers) {
            if (takeover.getUser().getId().equals(currentUserId)) {
                currentChain.add(takeover);
            } else {
                if (currentChain.size() > 1) {
                    chains.add(currentChain);
                }
                currentChain = new ArrayList<>();
                currentChain.add(takeover);
                currentUserId = takeover.getUser().getId();
            }
        }

        if (currentChain.size() > 1) {
            chains.add(currentChain);
        }

        return chains;
    }

}
