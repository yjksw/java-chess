package chess.model.unit;

import chess.model.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class KingTest {
    @Test
    void 킹_Navigator_생성_테스트() {
        Piece piece = new King(Side.BLACK);
        Square square = Square.of(Column.Col_2, Row.Row_G);
        int distance = 1;
        List<SquareNavigator> squareNavigators = new ArrayList<>();
        squareNavigators.add(new SquareNavigator(Direction.N, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.W, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.E, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.S, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.NW, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.NE, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.SW, square, distance));
        squareNavigators.add(new SquareNavigator(Direction.SE, square, distance));
        assertThat(new HashSet<>(piece.findSquareNavigators(square))).isEqualTo(
                new HashSet<>(squareNavigators));
    }

}