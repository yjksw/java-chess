package chess.domain.chess.game;

import chess.domain.chess.exception.*;
import chess.domain.chess.game.initializer.Initializer;
import chess.domain.chess.unit.King;
import chess.domain.chess.unit.Knight;
import chess.domain.chess.unit.Pawn;
import chess.domain.chess.unit.Unit;
import chess.domain.geometric.Direction;
import chess.domain.geometric.Position;
import chess.domain.geometric.Vector;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

public class ChessBoard {
    private Map<Position, Unit> units;
    private Team present;

    public ChessBoard(Initializer initializer) {
        this.units = initializer.create();
        this.present = initializer.createTeam();

    }

    public Optional<Unit> getUnit(Position position) {
        return Optional.ofNullable(units.get(position));
    }

    public void validateMove(Position source, Position target) {
        Optional<Unit> targetUnit = getUnit(target);
        Optional<Unit> sourceUnit = getUnit(source);
        Vector vector = Vector.of(source, target);

        if (!sourceUnit.isPresent()) {
            throw new SourceUnitNotPresentException("해당 위치에는 유닛이 존재하지 않습니다.");
        }

        if (sourceUnit.get().getTeam() != present) {
            throw new IllegalTurnException("현재는" + present.name() + " 턴입니다.");
        }

        if (targetUnit.isPresent() && sourceUnit.get().isEqualTeam(targetUnit.get())) {
            throw new SameTeamTargetUnitException("같은 팀을 공격할 수 없습니다.");
        }

        if (sourceUnit.get() instanceof Pawn) {
            Pawn pawn = (Pawn) sourceUnit.get();
            if (!pawn.validateDirection(source, target, targetUnit.isPresent())) {
                throw new PawnIllegalMovingRuleException("폰의 규칙에 어긋납니다.");
            }
            return;
        }

        if (!sourceUnit.get().validateDirection(vector)) {
            throw new IllegalMovingRuleException(sourceUnit.get().getName() + "의 규칙에 어긋납니다.");
        }
    }

    private void validateInterception(Position source, Position target) {
        Optional<Unit> sourceUnit = Optional.ofNullable(units.get(source));
        if (sourceUnit.get() instanceof Knight) {
            return;
        }

        Vector vector = Vector.of(source, target);
        Direction direction = Direction.of(vector);
        Position position = source;
        while (position.equals(target) == false) {
            position = direction.apply(position);
            Optional<Unit> unit = Optional.ofNullable(units.get(position));
            if (unit.isPresent() && (target.equals(position) == false)) {
                throw new UnitInterceptionAlongPathException("중간 경로에 유닛이 존재합니다.");
            }
        }
    }

    public void move(Position source, Position target) {
        validateMove(source, target);
        validateInterception(source, target);
        units.put(target, units.get(source));
        units.remove(source);
    }

    public Map<Position, Unit> getUnits() {
        return Collections.unmodifiableMap(units);
    }

    public Team getTeam() {
        return present;
    }

    public void changeTeam() {
        if (present == Team.WHITE) {
            present = Team.BLACK;
            return;
        }
        present = Team.WHITE;
    }

    public int numberOfKing() {
        int sum = 0;
        for (Position position : units.keySet()) {
            if (units.get(position) instanceof King) {
                sum += 1;
            }
        }
        return sum;
    }

    public Team getAliveKingTeam() {
        return units.get(units.keySet().stream().filter(key -> units.get(key) instanceof King)
                .findAny().get()).getTeam();
    }
}