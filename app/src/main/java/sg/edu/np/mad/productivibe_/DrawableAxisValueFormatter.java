package sg.edu.np.mad.productivibe_;

import android.graphics.drawable.Drawable;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

class DrawableAxisValueFormatter implements IAxisValueFormatter {
    private Drawable[] drawables;

    public DrawableAxisValueFormatter(Drawable[] drawables) {
        this.drawables = drawables;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        int index = (int) value;
        if (index >= 0 && index < drawables.length) {
            return ""; // Return an empty string since we don't want to display text labels
        }
        return "";
    }
}