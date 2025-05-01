package model;

import java.util.*;

import static java.util.Objects.*;

public class Board {

    private final List<List<Space>> spaces;

    // Construtor sem parâmetros para inicializar com valores padrão
    public Board() {
        this.spaces = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            List<Space> row = new ArrayList<>();
            for (int j = 0; j < 9; j++) {
                row.add(new Space(0, false)); // Inicializa com valor 0 e fixo como false
            }
            spaces.add(row);
        }
    }

    // Construtor com parâmetros para inicializar com espaços específicos
    public Board(final List<List<Space>> spaces) {
        this.spaces = spaces;
    }

    public List<List<Space>> getSpaces() {
        return spaces;
    }

    public GameStatusEnum getStatus() {
        boolean started = spaces.stream()
            .flatMap(Collection::stream)
            .anyMatch(s -> !s.isFixed() && nonNull(s.getActual()));
        if (!started) return GameStatusEnum.NON_STARTED;

        boolean incomplete = spaces.stream()
            .flatMap(Collection::stream)
            .anyMatch(s -> isNull(s.getActual()));
        return incomplete ? GameStatusEnum.INCOMPLETE : GameStatusEnum.COMPLETE;
    }

    public boolean isComplete() {
        for (List<Space> row : spaces) {
            for (Space s : row) {
                if (s.getActual() == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean hasErrors() {
        return hasRowErrors() || hasColErrors() || hasBlockErrors();
    }

    private boolean hasRowErrors() {
        for (List<Space> row : spaces) {
            Set<Integer> seen = new HashSet<>();
            for (Space s : row) {
                Integer value = s.getActual();
                if (value != null && !seen.add(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasColErrors() {
        for (int col = 0; col < 9; col++) {
            Set<Integer> seen = new HashSet<>();
            for (int row = 0; row < 9; row++) {
                Integer value = spaces.get(row).get(col).getActual();
                if (value != null && !seen.add(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean hasBlockErrors() {
        for (int blockRow = 0; blockRow < 3; blockRow++) {
            for (int blockCol = 0; blockCol < 3; blockCol++) {
                Set<Integer> seen = new HashSet<>();
                for (int r = blockRow * 3; r < (blockRow + 1) * 3; r++) {
                    for (int c = blockCol * 3; c < (blockCol + 1) * 3; c++) {
                        Integer value = spaces.get(r).get(c).getActual();
                        if (value != null && !seen.add(value)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public boolean changeValue(final int row, final int col, final Integer value) {
        Space space = spaces.get(row).get(col);
        if (space.isFixed()) return false;
        space.setActual(value);
        return true;
    }

    public boolean clearValue(final int row, final int col) {
        Space space = spaces.get(row).get(col);
        if (space.isFixed()) return false;
        space.clearSpace();
        return true;
    }

    public void reset() {
        for (List<Space> row : spaces) {
            for (Space s : row) {
                s.clearSpace();
            }
        }
    }

    public boolean gameIsFinished() {
        return getStatus() == GameStatusEnum.COMPLETE && !hasErrors();
    }
}
