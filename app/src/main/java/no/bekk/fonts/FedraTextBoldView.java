package no.bekk.fonts;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

class FedraTextBoldView extends TextView {

    public FedraTextBoldView(Context context, AttributeSet attr) {
        super(context, attr);
        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/FedraSansAltPro-Bold.otf"));
    }
}
