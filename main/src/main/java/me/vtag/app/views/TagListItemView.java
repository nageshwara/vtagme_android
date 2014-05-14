package me.vtag.app.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import me.vtag.app.CustomImageView;
import me.vtag.app.R;
import me.vtag.app.WelcomeActivity;
import me.vtag.app.backend.models.BaseTagModel;

/**
 * Created by nageswara on 5/3/14.
 */
public class TagListItemView extends FrameLayout implements View.OnClickListener {

    private View view;
    private BaseTagModel model;

    public TagListItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public TagListItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagListItemView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        view = inflate(getContext(), R.layout.tagcard, null);
        addView(view);
        this.setOnClickListener(this);
    }

    public void setModel(BaseTagModel model) {
        this.model = model;

        final Button button1 = (Button) findViewById(R.id.tagOptionsButton);
        button1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Creating the instance of PopupMenu
                PopupMenu popup = new PopupMenu(getContext(), button1);
                //Inflating the Popup using xml file
                popup.getMenuInflater().inflate(R.menu.subscribe_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(getContext(),"You Clicked : " + item.getTitle(),Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

//                Toast.makeText(getContext(), "You Clicked the button Yea!!!", Toast.LENGTH_SHORT).show();


                popup.show();//showing popup menu
            }
        });//closing the setOnClickListener method


        ImageView image = (ImageView) view.findViewById(R.id.imageView);
        TextView text = (TextView) view.findViewById(R.id.tagTitleView);

        text.setText(model.tag);
        Picasso.with(this.getContext()).load(model.videodetails.get(0).video.thumb)
                .resizeDimen(R.dimen.videolist_thumb_width, R.dimen.videolist_thumb_height)
                .centerInside()
                .into(image);
    }

    public BaseTagModel getModel() {
        return model;
    }

    @Override
    public void onClick(View view) {
        if (model != null) {
            if (getContext() instanceof  WelcomeActivity) {
                ((WelcomeActivity) getContext()).browseHashTag(model.tag);
            }
        }
    }
}
