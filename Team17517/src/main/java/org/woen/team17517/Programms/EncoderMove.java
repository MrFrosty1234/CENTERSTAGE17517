package org.woen.team17517.Programms;

public class EncoderMove {
    private Runnable[] actions;
    public EncoderMove(Runnable[] actions)
    {
        setActions(actions);
    }
    public void setActions(Runnable[] actions){
        this.actions = actions;
    }

    public Runnable[] getActions() {
        return actions;
    }
}
