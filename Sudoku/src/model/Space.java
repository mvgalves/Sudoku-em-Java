package model;

public class Space {

    private Integer actual;
    private final int expected;
    private final boolean fixed;

    public Space(final int expected, final boolean fixed) {
        this.expected = expected;
        this.fixed = fixed;
        this.actual = fixed ? expected : null;
    }

    public Integer getActual() {
        return actual;
    }

    public void setActual(Integer actual) {
        if (!fixed) {
            this.actual = actual;
        }
    }

    public void clearSpace() {
        if (!fixed) {
            this.actual = null;
        }
    }

    public int getExpected() {
        return expected;
    }

    public boolean isFixed() {
        return fixed;
    }

    public boolean isCorrect() {
        return expected == actual;
    }
}
