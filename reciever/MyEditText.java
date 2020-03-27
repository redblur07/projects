package com.gill.reciever;

import android.content.Context;
import android.widget.EditText;
import android.view.ViewGroup.LayoutParams;
import android.text.method.ScrollingMovementMethod;

public class MyEditText extends EditText{
//-2 for wrap_content, -1 for match_parent;
int width=-2;
int height=-2;
String text="TextBox";
LayoutParams params;

public MyEditText(Context context){
  super(context);
  createLayout();
}

public MyEditText(Context context, String t){
  super(context);
  this.text=t;
  createLayout();
}

public MyEditText(Context context, int w, int h){
  super(context);
  this.width=w;
  this.height=h;
  createLayout();
}

public MyEditText(Context context, String t, int w, int h){
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
  setGravity(1);
  setText(text);
  setMovementMethod(new ScrollingMovementMethod());
}
}
