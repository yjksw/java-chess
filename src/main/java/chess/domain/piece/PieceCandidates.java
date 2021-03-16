package chess.domain.piece;

public enum PieceCandidates {
    KING("K"),
    QUEEN("Q"),
    KNIGHT("N"),
    BISHOP("B"),
    ROOK("R"),
    PAWN("P"),
    VOID(".");

    private final String symbol;

    PieceCandidates(String symbol) {
        this.symbol = symbol;
    }

    public String getName(PieceColor pieceColor) {
        if (pieceColor == PieceColor.WHITE) {
            return symbol.toLowerCase();
        }

        return symbol;
    }
}
