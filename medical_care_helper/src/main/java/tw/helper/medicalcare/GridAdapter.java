package tw.helper.medicalcare;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

class GridAdapter extends BaseAdapter {

    private final Integer[] imgThumbIds;
    private final Context context ;

    public GridAdapter(Context c, Integer[] thumbImgArr) {
        context = c;
        imgThumbIds = thumbImgArr;
    }
    @Override
    public int getCount() {
        //傳回圖片數
        return imgThumbIds.length;
    }

    @Override
    public Object getItem(int position) {
        //傳回每個圖片的物件
        return null;
    }
    @Override
    public long getItemId(int position) {
        //慱回選擇圖片的ID
        return 0;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //傳回IMAGEVIEW物件
        ImageView imageView;
        if (convertView == null) {  // 是否需初始ImageView元件
            imageView = new ImageView(context);
            imageView.setLayoutParams(new GridView.LayoutParams(120, 90));
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            imageView.setPadding(1, 1, 1, 1);
            imageView.setImageResource(imgThumbIds[position]);
        } else {
            imageView = (ImageView) convertView;
        }
        return imageView;
    }
}
