package pl.marek1and.myworktime.transport;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import pl.marek1and.myworktime.BaseDialog;
import pl.marek1and.myworktime.R;
import pl.marek1and.myworktime.db.beans.Transport;

public class TransportChooserDialog extends BaseDialog implements View.OnClickListener, AdapterView.OnItemClickListener {

    private Context context;
    private ListView transportsView;
    private Button cancelButton;
    private Button applyButton;
    private TransportListChangeListener changeListener;

    private Set<Transport> selectedTransports;

    public TransportChooserDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.transport_chooser_dialog);
        setDialogSize();
        setTitle(R.string.choose_transport);
        transportsView = (ListView) findViewById(R.id.tcd_transport_list);
        cancelButton = (Button) findViewById(R.id.tcd_cancel_button);
        applyButton = (Button) findViewById(R.id.tcd_apply_button);

        transportsView.setAdapter(new TransportItemAdapter(context, selectedTransports));
        transportsView.setOnItemClickListener(this);
        cancelButton.setOnClickListener(this);
        applyButton.setOnClickListener(this);
    }

    public void setChangeListener(TransportListChangeListener changeListener) {
        this.changeListener = changeListener;
    }

    public void setSelectedTransports(Set<Transport> selectedTransports) {
        this.selectedTransports = selectedTransports;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.tcd_apply_button:
                if(changeListener != null) {
                    changeListener.transportListChanged(getSelectedTransports());
                }
                hide();
                break;
            case R.id.tcd_cancel_button:
                cancel();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TransportItemAdapter adapter = (TransportItemAdapter)parent.getAdapter();
        Transport t = (Transport)parent.getItemAtPosition(position);
        adapter.changeTransportSelection(t);
        adapter.notifyDataSetChanged();
    }

    public Set<Transport> getSelectedTransports() {
        TransportItemAdapter adapter = (TransportItemAdapter)transportsView.getAdapter();
        return adapter.getSelectedTransports();
    }

    private static class TransportItemAdapter extends ArrayAdapter<Transport> {

        private Context context;
        private Set<Transport> items;
        private Map<Transport, Boolean> itemSelectionMap;
        private Set<Transport> selectedTransports;

        public TransportItemAdapter(Context context, Set<Transport> selectedTransports) {
            super(context, R.layout.transport_list_item, new ArrayList<>(EnumSet.allOf(Transport.class)));
            this.context = context;
            this.items = EnumSet.allOf(Transport.class);
            this.selectedTransports = selectedTransports;
            prepareSelectionMap();
        }

        private void prepareSelectionMap() {
            itemSelectionMap = new HashMap<>();
            for (Transport t : items) {
                itemSelectionMap.put(t, selectedTransports == null ? false : selectedTransports.contains(t));
            }

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.transport_list_item, parent, false);

            CheckBox checkbox = (CheckBox) rowView.findViewById(R.id.tcd_transport_list_checkbox);
            ImageView imageView = (ImageView) rowView.findViewById(R.id.tcd_transport_list_image);
            TextView textView = (TextView) rowView.findViewById(R.id.tcd_transport_list_text);

            Transport transport = (Transport)items.toArray()[position];
            int id = transport.getIconIdentifier(context);
            imageView.setImageResource(id);

            textView.setText(transport.getName(context));
            checkbox.setChecked(isTransportSelected(transport));

            return rowView;
        }

        private boolean isTransportSelected(Transport transport) {
            return itemSelectionMap.get(transport);
        }

        public void changeTransportSelection(Transport transport) {
            itemSelectionMap.put(transport, !itemSelectionMap.get(transport));
        }

        public Set<Transport> getSelectedTransports() {
            Set<Transport> transports = EnumSet.noneOf(Transport.class);
            for(Map.Entry<Transport,Boolean> entries: itemSelectionMap.entrySet()) {
                Transport t = entries.getKey();
                boolean checked = entries.getValue();
                if(checked) {
                    transports.add(t);
                }
            }
            return transports;
        }

    }

}
