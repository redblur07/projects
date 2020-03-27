package com.gill.reciever;

import android.content.Context;
import android.widget.Button;
import android.view.View;
//import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;

public class MyButton extends Button{
//-2 for wrap_content, -1 for match_parent;
int width=-2;
int height=-2;
String text="Button";
LayoutParams params;

public MyButton(Context context){
  super(context);
  createLayout();
}

public MyButton(Context context, String t){
  super(context);
  this.text=t;
  createLayout();
}

public MyButton(Context context, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
  createLayout();
}

public MyButton(Context context, String t, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
  this.text=t;
  createLayout();
}

public void setSize(int w, int h){
  this.width=w;
  this.height=h;
  params=new LayoutParams(w, h);
  setLayoutParams(params);
}

public void createLayout(){
  setSize(width, height);
  setPadding(1,1,1,1);
  setText(text);
  
  setOnClickListener(new OnClickListener(){
    @Override
    public void onClick(View v){
    onClicked();
    }
  });
}

public void onClicked(){
  
}
}
