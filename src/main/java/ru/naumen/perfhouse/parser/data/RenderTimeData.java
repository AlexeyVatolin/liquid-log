package ru.naumen.perfhouse.parser.data;

public class RenderTimeData extends Data{
    private int renderTime;

    public int getRenderTime() {
        return renderTime;
    }

    public void setRenderTime(int renderTime) {
        this.renderTime = renderTime;
    }

    public boolean isEmpty()
    {
        return renderTime == 0;
    }
}
