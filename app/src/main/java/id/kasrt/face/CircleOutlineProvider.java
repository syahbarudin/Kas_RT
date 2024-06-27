package id.kasrt.face;

import android.graphics.Outline;
import android.view.View;
import android.view.ViewOutlineProvider;

public class CircleOutlineProvider extends ViewOutlineProvider {
    @Override
    public void getOutline(View view, Outline outline) {
        int diameter = Math.min(view.getWidth(), view.getHeight());
        outline.setOval(0, 0, diameter, diameter);
    }
}
