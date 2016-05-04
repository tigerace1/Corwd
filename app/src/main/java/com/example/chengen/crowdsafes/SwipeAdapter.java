package com.example.chengen.crowdsafes;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
public class SwipeAdapter extends PagerAdapter {
    private ArrayList<Bitmap> imageForShowing;
    private Context ctx;
    private LayoutInflater layoutInflater;
    public SwipeAdapter(Context ctx,Bitmap one,Bitmap two, Bitmap three){
        this.ctx = ctx;
        imageForShowing = new ArrayList<>();
        if(one!=null)
            imageForShowing.add(one);
        if(two!=null)
            imageForShowing.add(two);
        if(three!=null)
            imageForShowing.add(three);
    }
    @Override
    public int getCount() {
        return imageForShowing.size();
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        layoutInflater=(LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View item_view = layoutInflater.inflate(R.layout.swipe_layout,container,false);
        ImageView imageView = (ImageView)item_view.findViewById(R.id.ivBigPicture);
        imageView.setImageDrawable(new BitmapDrawable(imageForShowing.get(position)));
        container.addView(item_view);
        return item_view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
        //super.destroyItem(container, position, object);
    }
}
