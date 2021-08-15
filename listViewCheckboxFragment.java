package com.finalproject3.amir.testgooglemaps;

/**
 * Created by amir on 5/20/2016.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by amir on 5/9/2016.
 */
public class listViewCheckboxFragment extends Fragment {
    public static final String TAG = listViewCheckboxFragment.class.getSimpleName();
    MyCustomAdapter dataAdapter = null;
    GoogleMapViewFragment map ;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        ArrayList<deliveris> deliveryArray = new ArrayList<deliveris>();
        FragmentManager fm = getFragmentManager();
        GoogleMapViewFragment map = (GoogleMapViewFragment)fm.findFragmentByTag(GoogleMapViewFragment.TAG);
        deliveryArray = map.getDeliveriesArray();




        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getActivity(),
                R.layout.delivery_info, deliveryArray);


        View v =  inflater.inflate(
                R.layout.fragment_list_view_checkbox, container, false);


        ListView listView = (ListView) v.findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                deliveris delivery = (deliveris) parent.getItemAtPosition(position);
                Toast.makeText(getActivity(),
                        "Clicked on Row: " + delivery.getPlace(),
                        Toast.LENGTH_LONG).show();
            }
        });




        Button myButton = (Button) v.findViewById(R.id.findSelected);
        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                StringBuffer responseText = new StringBuffer();
//                responseText.append("The following were selected...\n");
//
//                ArrayList<deliveris> deliveryListFinal = new ArrayList<deliveris>();
//                ArrayList<deliveris> deliveryList = dataAdapter.deliveryList;
//                for (int i = 0; i < deliveryList.size(); i++) {
//                    deliveris delivery = deliveryList.get(i);
//                    if (delivery.isChecked()) {
//                        deliveryListFinal.add(delivery);
//                    }
////                    map.setDeliveriesArray(deliveryListFinal);
//                }
//
//                Toast.makeText(getActivity(),
//                        responseText, Toast.LENGTH_LONG).show();
                FragmentManager fm = getFragmentManager();
//                FragmentTransaction fragmentTransaction = fm.beginTransaction();
//                fragmentTransaction.add(R.id.FragmentsContainer, fr );
                fm.popBackStack();

            }
        });


        return v;

    }



    private class MyCustomAdapter extends ArrayAdapter<deliveris> {
        private ArrayList<deliveris> deliveryList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<deliveris> deliveryList) {
            super(context, textViewResourceId, deliveryList);
            this.deliveryList = new ArrayList<deliveris>();
            this.deliveryList.addAll(deliveryList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.delivery_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView.findViewById(R.id.checkBox1);
                convertView.setTag(holder);

                holder.name.setOnClickListener( new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v ;
                        deliveris delivery = (deliveris) cb.getTag();
                        Toast.makeText(getActivity(),
                                "Clicked on Checkbox: " + cb.getText() +
                                        " is " + cb.isChecked(),
                                Toast.LENGTH_LONG).show();
                        delivery.setChecked(cb.isChecked());
                    }
                });
            }
            else {
                holder = (ViewHolder) convertView.getTag();
            }

            deliveris delivery = deliveryList.get(position);
            holder.code.setText(" (" +  delivery.getPlace() + ")");
            holder.name.setText("time");
            holder.name.setChecked(delivery.isChecked());
            holder.name.setTag(delivery);

            return convertView;

        }

    }





}
