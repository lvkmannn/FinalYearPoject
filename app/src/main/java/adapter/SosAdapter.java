package adapter;

import android.app.Activity;
import android.content.Context;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import com.example.finalyearproject.R;

import java.util.List;

public class SosAdapter extends ArrayAdapter {

    // Contact data
    private String fireStation[] = {"Balai Bomba dan Penyelamat Shah Alam", "Balai Bomba dan Penyelamat Kuala Kubu Bharu",
    "Balai Bomba dan Penyelamat, Tanjung Karang", "Balai Bomba dan Penyelamat Bestari Jaya", "Balai Bomba dan Penyelamat, Kuala Selangor",
    "Balai Bomba dan Penyelamat Batu Arang", "Balai Bomba dan Penyelamat Rawang", "Balai Bomba dan Penyelamat Ampang",
    "Balai Bomba dan Penyelamat  Sabak Bernam", "Balai Bomba dan Penyelamat Klang Utara"};

    private String district[] = {"Shah Alam", "Kuala Kubu Bharu", "Tanjung Karang", "Bestari Jaya", "Kuala Selangor",
    "Batu Arang", "Rawang", "Ampang", "Sabak Bernam", "Klang Utara"};

    private String phoneNum[] = {"03 – 5519 4444", "03 – 6064 1444", "03- 3269 4424", "03 – 3271 9444", "03 – 3289 1444", " 03 – 6035 2444",
    "03 – 6092 4444", "03 – 4292 4444", "03 – 3216 1444", "03 – 3371 4444"};


    public SosAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }

    public SosAdapter(@NonNull Context context, int resource, int textViewResourceId) {
        super(context, resource, textViewResourceId);
    }

    public SosAdapter(@NonNull Context context, int resource, @NonNull Object[] objects) {
        super(context, resource, objects);
    }

    public SosAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull Object[] objects) {
        super(context, resource, textViewResourceId, objects);
    }

    public SosAdapter(@NonNull Context context, int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    public SosAdapter(@NonNull Context context, int resource, int textViewResourceId, @NonNull List objects) {
        super(context, resource, textViewResourceId, objects);
    }
    // Contact Data
}
