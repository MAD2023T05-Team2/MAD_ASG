/*package com.example.mad_asg;

public class DestressAdapter extends RecyclerView.Apapter<DestressViewHolder> {

    private ArrayList<> list_objects;

    public DestressAdapter(ArrayList<>obj){
        this.list_objects = obj;
    }
    public DestressViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_pictures, parent, false);
        DestressViewHolder holder = new DestressViewHolder(view);

        return holder;
    }

    public void onBindViewHolder(DestressViewHolder holder, int position){
        list_items = list_objects.get(position);
        holder.txt.setText(list_items.getMyText());
        holder.image.setImageResource(list_items.getMyImageID());
    }
    public int getItemCount(){
        return list_objects.size();
    }
}
*/