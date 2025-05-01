package model;

public enum GameStatusEnum {
    NON_STARTED("Não Iniciado"),
    INCOMPLETE("Incompleto"),
    COMPLETE("Completo");

    private final String label;

    GameStatusEnum(final String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
