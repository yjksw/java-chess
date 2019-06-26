package chess.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CoordinatePairTest {

    @Test
    void invalidSymbol() {
        assertThat(CoordinatePair.of("z1").isPresent()).isFalse();
    }
}