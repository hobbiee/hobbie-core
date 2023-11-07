package com.br.hobbie.modules.event.application;

import com.br.hobbie.modules.event.domain.entities.Event;
import com.br.hobbie.modules.player.domain.entities.Player;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@RequiredArgsConstructor
@Validated
public class HaversineDistanceCalculator implements DistanceCalculator {

    @Override
    public boolean isWithinRadius(@NotNull Player player, @NotNull Event event) {
        double distance = getDistance(player.getMatchLatitude(), player.getMatchLongitude(), event.getLatitude(), event.getLongitude());
        return player.distanceIsWithinRadius(distance);
    }


}
